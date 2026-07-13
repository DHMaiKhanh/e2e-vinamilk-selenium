const STATUS_ICON = { passed: "✓", failed: "✕", broken: "⚠", skipped: "⊘" };

function formatDuration(ms) {
  if (ms == null) return "-";
  return `${(ms / 1000).toFixed(1)}s`;
}

export default function TestDetail({ test, onClose }) {
  if (!test) return null;
  const isFailure = test.status === "failed" || test.status === "broken";

  return (
    <div className="overlay" onClick={onClose}>
      <div className="detail-panel" onClick={(e) => e.stopPropagation()}>
        <button className="close-btn" onClick={onClose} aria-label="Đóng">
          ×
        </button>
        <h2>
          {STATUS_ICON[test.status]} {test.name}
        </h2>
        <div className="detail-meta">
          {test.testCase && <>Test case: <b>{test.testCase}</b> · </>}
          {test.className}.{test.method} · thời gian chạy: {formatDuration(test.durationMs)}
        </div>

        {isFailure && test.failLocation && (
          <>
            <div className="section-label">Fail tại vị trí code</div>
            <div className="fail-location">{test.failLocation.display}</div>
          </>
        )}

        {isFailure && test.projectFrames && test.projectFrames.length > 1 && (
          <>
            <div className="section-label">Chuỗi gọi trong code dự án (từ gần nhất đến xa nhất)</div>
            <div className="frame-list">
              {test.projectFrames.map((f, i) => (
                <div key={i}>
                  {i === 0 ? "→ " : "  ".repeat(i) + "↳ "}
                  {f}
                </div>
              ))}
            </div>
          </>
        )}

        {isFailure && test.errorMessage && (
          <>
            <div className="section-label">Lý do fail</div>
            <div className="error-box">{test.errorMessage}</div>
          </>
        )}

        {isFailure && (
          <>
            <div className="section-label">Ảnh chụp màn hình lúc fail</div>
            <div className="screenshot-wrap">
              {test.screenshot ? (
                <img src={`${import.meta.env.BASE_URL}${test.screenshot}`} alt={`Screenshot of ${test.name}`} />
              ) : (
                <div className="no-screenshot">
                  Không tìm thấy ảnh chụp màn hình khớp với test này.
                </div>
              )}
            </div>
          </>
        )}

        {isFailure && test.stackTrace && (
          <>
            <div className="section-label">Stack trace đầy đủ</div>
            <div className="stack-trace">{test.stackTrace}</div>
          </>
        )}
      </div>
    </div>
  );
}
