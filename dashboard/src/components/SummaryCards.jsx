const TILES = [
  { key: "total", label: "Tổng số test", dot: "total" },
  { key: "passed", label: "Pass", dot: "passed" },
  { key: "failed", label: "Fail", dot: "failed" },
  { key: "broken", label: "Broken", dot: "broken" },
  { key: "skipped", label: "Skipped", dot: "skipped" },
];

export default function SummaryCards({ summary }) {
  return (
    <div className="summary-row">
      {TILES.map((t) => (
        <div className="stat-tile" key={t.key}>
          <div className="label">
            <span className={`dot ${t.dot}`} />
            {t.label}
          </div>
          <div className="value">{summary[t.key] ?? 0}</div>
        </div>
      ))}
    </div>
  );
}
