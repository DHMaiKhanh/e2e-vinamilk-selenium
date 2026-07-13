// Aggregates ui-tests/target/allure-results into dashboard/public/report.json
// and copies matched failure screenshots into dashboard/public/screenshots/.
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.resolve(__dirname, "..", "..");
const RESULTS_DIR = path.join(ROOT, "ui-tests", "target", "allure-results");
const PUBLIC_DIR = path.join(__dirname, "..", "public");
const SCREENSHOTS_OUT = path.join(PUBLIC_DIR, "screenshots");

const PROJECT_PACKAGE = "com.vinamilk.automation";

function readJson(file) {
  return JSON.parse(fs.readFileSync(file, "utf8"));
}

function labelValue(labels, name) {
  const l = (labels || []).find((x) => x.name === name);
  return l ? l.value : null;
}

// Extracts the deepest (first-encountered) project stack frame from a trace,
// since that is the line of our own code closest to the actual failure.
function extractFailLocation(trace) {
  if (!trace) return null;
  const lines = trace.split(/\r?\n/);
  for (const line of lines) {
    const m = line.match(/^\s*at\s+([\w.$]+)\.([\w$<>]+)\(([^)]+)\)/);
    if (m && m[1].startsWith(PROJECT_PACKAGE)) {
      const [, className, methodName, fileRef] = m;
      const simpleClass = className.substring(className.lastIndexOf(".") + 1);
      return {
        className,
        methodName,
        file: fileRef,
        display: `${simpleClass}.${methodName}(${fileRef})`,
      };
    }
  }
  return null;
}

function extractProjectFrames(trace) {
  if (!trace) return [];
  const lines = trace.split(/\r?\n/);
  const frames = [];
  for (const line of lines) {
    const m = line.match(/^\s*at\s+([\w.$]+)\.([\w$<>]+)\(([^)]+)\)/);
    if (m && m[1].startsWith(PROJECT_PACKAGE)) {
      const simpleClass = m[1].substring(m[1].lastIndexOf(".") + 1);
      frames.push(`${simpleClass}.${m[2]}(${m[3]})`);
    }
  }
  return frames;
}

function main() {
  if (!fs.existsSync(RESULTS_DIR)) {
    console.error(`No allure-results found at ${RESULTS_DIR}. Run "mvn test" in ui-tests first.`);
    process.exit(1);
  }

  fs.mkdirSync(SCREENSHOTS_OUT, { recursive: true });
  for (const f of fs.readdirSync(SCREENSHOTS_OUT)) {
    fs.rmSync(path.join(SCREENSHOTS_OUT, f));
  }

  const allFiles = fs.readdirSync(RESULTS_DIR);
  const resultFiles = allFiles.filter((f) => f.endsWith("-result.json"));
  const attachmentFiles = allFiles.filter((f) => f.endsWith("-attachment"));

  const attachments = attachmentFiles
    .map((f) => {
      const full = path.join(RESULTS_DIR, f);
      const stat = fs.statSync(full);
      return { file: f, full, mtime: stat.mtimeMs, used: false };
    })
    .sort((a, b) => a.mtime - b.mtime);

  const rawTests = resultFiles.map((f) => readJson(path.join(RESULTS_DIR, f)));

  // Maven/TestNG re-runs and the RetryAnalyzer (up to 2 retries per flaky test)
  // each write a separate *-result.json with the same historyId. Keep only the
  // most recent attempt per historyId so one test case = one entry in the report.
  const byHistoryId = new Map();
  for (const t of rawTests) {
    const key = t.historyId || t.fullName || t.uuid;
    const prev = byHistoryId.get(key);
    if (!prev || (t.stop || 0) > (prev.stop || 0)) {
      byHistoryId.set(key, t);
    }
  }
  const tests = Array.from(byHistoryId.values());
  for (const t of tests) {
    const attempts = rawTests.filter((r) => (r.historyId || r.fullName || r.uuid) === (t.historyId || t.fullName || t.uuid));
    t.attemptCount = attempts.length;
  }

  // Match failing/broken tests to the nearest unused screenshot attachment
  // (by mtime proximity to the test's stop time). Allure attachment linkage
  // was found to be broken in this project's current TestListener, so this
  // heuristic recovers the pairing without touching Java code.
  const failing = tests
    .filter((t) => t.status === "failed" || t.status === "broken")
    .sort((a, b) => a.stop - b.stop);

  const WINDOW_MS = 10000;
  const matchedScreenshotByUuid = {};
  for (const t of failing) {
    let best = null;
    let bestDelta = Infinity;
    for (const att of attachments) {
      if (att.used) continue;
      const delta = Math.abs(att.mtime - t.stop);
      if (delta < bestDelta && delta <= WINDOW_MS) {
        best = att;
        bestDelta = delta;
      }
    }
    if (best) {
      best.used = true;
      const destName = `${t.uuid}.png`;
      fs.copyFileSync(best.full, path.join(SCREENSHOTS_OUT, destName));
      matchedScreenshotByUuid[t.uuid] = `screenshots/${destName}`;
    }
  }

  const featuresMap = new Map();
  for (const t of tests) {
    const feature = labelValue(t.labels, "feature") || labelValue(t.labels, "testClass") || "Uncategorized";
    const epic = labelValue(t.labels, "epic") || "";
    if (!featuresMap.has(feature)) {
      featuresMap.set(feature, { feature, epic, tests: [] });
    }
    const failLocation = t.statusDetails ? extractFailLocation(t.statusDetails.trace) : null;
    const projectFrames = t.statusDetails ? extractProjectFrames(t.statusDetails.trace) : [];

    featuresMap.get(feature).tests.push({
      id: t.uuid,
      name: t.name,
      testCase: t.description || null,
      className: labelValue(t.labels, "testClass"),
      method: labelValue(t.labels, "testMethod"),
      status: t.status,
      durationMs: t.stop && t.start ? t.stop - t.start : null,
      start: t.start,
      stop: t.stop,
      errorMessage: t.statusDetails ? t.statusDetails.message : null,
      stackTrace: t.statusDetails ? t.statusDetails.trace : null,
      failLocation,
      projectFrames,
      screenshot: matchedScreenshotByUuid[t.uuid] || null,
      attemptCount: t.attemptCount || 1,
    });
  }

  const features = Array.from(featuresMap.values())
    .map((f) => {
      f.tests.sort((a, b) => (a.name || "").localeCompare(b.name || ""));
      const counts = { passed: 0, failed: 0, broken: 0, skipped: 0 };
      for (const t of f.tests) counts[t.status] = (counts[t.status] || 0) + 1;
      return { ...f, total: f.tests.length, ...counts };
    })
    .sort((a, b) => a.feature.localeCompare(b.feature));

  const summary = { total: tests.length, passed: 0, failed: 0, broken: 0, skipped: 0 };
  for (const t of tests) summary[t.status] = (summary[t.status] || 0) + 1;
  summary.passRate = summary.total ? Math.round((summary.passed / summary.total) * 1000) / 10 : 0;

  const report = {
    generatedAt: new Date(Math.max(...tests.map((t) => t.stop || 0), Date.now())).toISOString(),
    summary,
    features,
  };

  fs.writeFileSync(path.join(PUBLIC_DIR, "report.json"), JSON.stringify(report, null, 2));
  const matchedCount = Object.keys(matchedScreenshotByUuid).length;
  console.log(
    `Report built: ${summary.total} tests (${summary.passed} passed, ${summary.failed} failed, ${summary.broken} broken, ${summary.skipped} skipped).`
  );
  console.log(`Matched ${matchedCount}/${failing.length} failing tests to a screenshot.`);
}

main();
