const STATUS_COLORS = {
  passed: "var(--status-good)",
  failed: "var(--status-critical)",
  broken: "var(--status-serious)",
  skipped: "var(--text-muted)",
};

const STATUS_LABELS = {
  passed: "Passed",
  failed: "Failed",
  broken: "Broken (lỗi ngoài ý muốn)",
  skipped: "Skipped",
};

export default function DonutChart({ summary }) {
  const segments = ["passed", "failed", "broken", "skipped"]
    .map((key) => ({ key, value: summary[key] || 0 }))
    .filter((s) => s.value > 0);

  const total = summary.total || 1;
  const size = 140;
  const stroke = 20;
  const r = (size - stroke) / 2;
  const circumference = 2 * Math.PI * r;

  let offsetAcc = 0;
  const arcs = segments.map((s) => {
    const fraction = s.value / total;
    const dash = fraction * circumference;
    const arc = {
      key: s.key,
      value: s.value,
      dasharray: `${dash} ${circumference - dash}`,
      dashoffset: -offsetAcc,
    };
    offsetAcc += dash;
    return arc;
  });

  return (
    <div className="donut-section">
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke="var(--gridline)"
          strokeWidth={stroke}
        />
        {arcs.map((a) => (
          <circle
            key={a.key}
            cx={size / 2}
            cy={size / 2}
            r={r}
            fill="none"
            stroke={STATUS_COLORS[a.key]}
            strokeWidth={stroke}
            strokeDasharray={a.dasharray}
            strokeDashoffset={a.dashoffset}
            transform={`rotate(-90 ${size / 2} ${size / 2})`}
          />
        ))}
        <text
          x={size / 2}
          y={size / 2 - 4}
          textAnchor="middle"
          fontSize="22"
          fontWeight="600"
          fill="var(--text-primary)"
        >
          {summary.passRate}%
        </text>
        <text
          x={size / 2}
          y={size / 2 + 16}
          textAnchor="middle"
          fontSize="11"
          fill="var(--text-muted)"
        >
          pass rate
        </text>
      </svg>
      <div className="legend">
        {segments.map((s) => (
          <div className="legend-item" key={s.key}>
            <span className="dot" style={{ background: STATUS_COLORS[s.key] }} />
            {STATUS_LABELS[s.key]}: <b>{s.value}</b>
          </div>
        ))}
      </div>
    </div>
  );
}
