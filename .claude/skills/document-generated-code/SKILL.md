---
name: document-generated-code
description: After the generate-test-code skill has produced/updated the Page Object and TestNG test class for a screen, write a Markdown analysis file documenting exactly what code was generated for that screen (methods, locators, test cases, coverage vs the spec). Use when the user asks to "phân tích code vừa gen", "tạo file .md phân tích màn hình này", or wants a written record of generated test code right after code generation.
---

# Document Generated Code

Produces a Markdown analysis of the Java automation code that already exists for one screen —
run this right after `generate-test-code` (or any time the user wants an up-to-date write-up of a
screen's current Page Object + test class), never before code exists.

## Inputs

- A page name or spec path (e.g. `docs/features/home-page.md`). Resolve to the matching Page
  Object (`ui-tests/src/main/java/com/vinamilk/automation/ui/pages/<Page>.java`) and test class(es)
  (`ui-tests/src/test/java/com/vinamilk/automation/ui/tests/<Page>Test.java`, plus any other test
  class that imports/uses that Page Object, e.g. `AddToCartTest.java` may use `HomePage`).

## Steps

1. **Locate the code.** Read the full Page Object and every test class that exercises it. If
   nothing has been generated yet for this page, stop and tell the user to run `generate-test-code`
   first — don't analyze a spec instead of real code.
2. **Read the source spec if it exists** (`docs/features/<page-slug>.md`) so the analysis can
   report coverage against it, but the subject of this document is the code, not the spec.
3. **Write the analysis** to `docs/analysis/<page-slug>-analysis.md` (create the `docs/analysis/`
   folder if missing). Structure:
   - Header: `# <Page Name> — Phân tích code đã generate`, timestamp, list of source files analyzed
     (Page Object path + line count, each test class path + line count).
   - `## Tổng quan` — one paragraph: what the Page Object represents, how many locators, how many
     public methods, how many `@Test` methods across which test classes.
   - `## Locators` — table: locator name, `By` strategy/selector, what it targets.
   - `## Methods (Page Object)` — table: method name, signature/return type, what it does, which
     locator(s) it uses.
   - `## Test Cases (đã generate)` — table: test method name, file, Allure `@Feature`/`@Severity`,
     which spec `TC-<PAGE>-NN` id it maps to (if a spec exists), one-line summary of what it
     asserts.
   - `## Coverage vs. spec` (only if a spec file exists) — list any `TC-<PAGE>-NN` rows from the
     spec that have **no** corresponding generated test, and any generated tests that don't map to
     a spec row (extra coverage). If no spec exists, state that explicitly instead of guessing.
   - `## Giải thích chi tiết từng đoạn code` — walk through the Page Object and each test class
     block by block, in source order. For each logical block (locator declarations, constructor,
     each method, each `@Test`), include:
     - A fenced code snippet quoting the actual lines from the file (with a `path:startLine-endLine`
       caption above it).
     - A plain-language explanation directly under it: what the block does, why it's written that
       way (e.g. explicit wait before click, why a particular locator strategy was chosen), and how
       it fits into the overall flow (e.g. "method này được gọi trong TC-HOME-03 để..."). Explain
       Selenium/TestNG/Allure-specific constructs (`WebDriverWait`, `ExpectedConditions`,
       `@Step`, `@DataProvider`, etc.) in plain terms rather than assuming the reader knows them.
     - Do this for every method/test, not just a representative sample — the goal is a reader who
       has never seen the file being able to follow it end-to-end from this section alone.
   - `## Nhận xét / rủi ro` — brief, concrete notes only: fragile locators (e.g. `nth-of-type`,
     index-based), hard-coded waits, assertions that only check visibility and not content, missing
     negative-path coverage — grounded in what's actually in the file, not generic advice.
4. **Report back** the output file path and a 2-3 sentence summary of coverage (e.g. "18/20 TC
   implemented, 2 missing: TC-HOME-07, TC-HOME-15").

## Conventions to preserve

- This skill never edits `ui-tests/src/**` — it only reads code and writes one Markdown file.
- One screen = one analysis file at `docs/analysis/<page-slug>-analysis.md`; re-running this skill
  overwrites/updates that same file in place rather than creating a dated copy.
- Write in Vietnamese, consistent with the rest of `docs/`.
- Ground every claim in the actual file contents you read — don't restate the spec as if it were
  the implementation.
