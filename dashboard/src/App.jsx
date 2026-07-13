import { useEffect, useMemo, useState } from "react";
import SummaryCards from "./components/SummaryCards.jsx";
import DonutChart from "./components/DonutChart.jsx";
import FeatureList from "./components/FeatureList.jsx";
import TestDetail from "./components/TestDetail.jsx";

const FILTERS = [
  { key: "all", label: "Tất cả" },
  { key: "failed", label: "Fail" },
  { key: "broken", label: "Broken" },
  { key: "passed", label: "Pass" },
  { key: "skipped", label: "Skipped" },
];

export default function App() {
  const [report, setReport] = useState(null);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState("all");
  const [selectedTest, setSelectedTest] = useState(null);

  useEffect(() => {
    fetch(`${import.meta.env.BASE_URL}report.json`)
      .then((r) => {
        if (!r.ok) throw new Error(`report.json not found (${r.status})`);
        return r.json();
      })
      .then(setReport)
      .catch((e) => setError(e.message));
  }, []);

  const filteredFeatures = useMemo(() => {
    if (!report) return [];
    if (filter === "all") return report.features;
    return report.features
      .map((f) => ({ ...f, tests: f.tests.filter((t) => t.status === filter) }))
      .filter((f) => f.tests.length > 0);
  }, [report, filter]);

  if (error) {
    return (
      <div className="empty-state">
        Chưa có báo cáo. Hãy chạy <code>npm run report</code> sau khi chạy <code>mvn test</code> trong
        ui-tests. ({error})
      </div>
    );
  }

  if (!report) {
    return <div className="empty-state">Đang tải report.json...</div>;
  }

  return (
    <>
      <div className="header">
        <h1>Vinamilk E2E Test Dashboard</h1>
        <span className="generated-at">
          Lần chạy gần nhất: {new Date(report.generatedAt).toLocaleString("vi-VN")}
        </span>
      </div>

      <SummaryCards summary={report.summary} />
      <DonutChart summary={report.summary} />

      <div className="filter-row">
        {FILTERS.map((f) => (
          <button
            key={f.key}
            className={`filter-chip ${filter === f.key ? "active" : ""}`}
            onClick={() => setFilter(f.key)}
          >
            {f.label}
          </button>
        ))}
      </div>

      <FeatureList features={filteredFeatures} onSelectTest={setSelectedTest} />

      <TestDetail test={selectedTest} onClose={() => setSelectedTest(null)} />
    </>
  );
}
