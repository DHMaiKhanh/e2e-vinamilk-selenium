import { useState } from "react";

const STATUS_ICON = { passed: "✓", failed: "✕", broken: "⚠", skipped: "⊘" };

function formatDuration(ms) {
  if (ms == null) return "-";
  return `${(ms / 1000).toFixed(1)}s`;
}

function FeatureCard({ feature, onSelectTest, defaultOpen }) {
  const [open, setOpen] = useState(defaultOpen);

  return (
    <div className="feature-card">
      <div className="feature-header" onClick={() => setOpen((o) => !o)}>
        <div className="feature-title">
          <span className="name">
            {open ? "▾" : "▸"} {feature.feature}
          </span>
          {feature.epic && <span className="epic">{feature.epic}</span>}
        </div>
        <div className="feature-counts">
          <span>{feature.total} test cases</span>
          {feature.passed > 0 && <span className="badge passed">{feature.passed} pass</span>}
          {feature.failed > 0 && <span className="badge failed">{feature.failed} fail</span>}
          {feature.broken > 0 && <span className="badge broken">{feature.broken} broken</span>}
          {feature.skipped > 0 && <span className="badge skipped">{feature.skipped} skipped</span>}
        </div>
      </div>
      {open && (
        <div className="test-list">
          {feature.tests.map((t) => (
            <div className="test-row" key={t.id} onClick={() => onSelectTest(t)}>
              <span className="status-icon">{STATUS_ICON[t.status]}</span>
              <span className="test-name">{t.name}</span>
              {t.testCase && <span className="test-case-id">{t.testCase}</span>}
              <span className="duration">{formatDuration(t.durationMs)}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default function FeatureList({ features, onSelectTest }) {
  if (features.length === 0) {
    return <div className="empty-state">Không có test nào khớp với bộ lọc hiện tại.</div>;
  }
  return (
    <>
      {features.map((f, i) => (
        <FeatureCard key={f.feature} feature={f} onSelectTest={onSelectTest} defaultOpen={i === 0} />
      ))}
    </>
  );
}
