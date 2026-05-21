import { useState } from "react";
import axios from "axios";
import "./App.css";

const API_BASE = "http://localhost:8080";

const units = {
  LengthUnit: ["FEET", "INCH", "YARDS", "CENTIMETERS"],
WeightUnit: ["KILOGRAM","GRAM","POUND"],
  VolumeUnit: ["GALLON", "LITRE", "MILLILITRE"],
  TemperatureUnit: ["CELSIUS", "FAHRENHEIT", "KELVIN"],
};

const measurementTypes = [
  { key: "LengthUnit", label: "Length", icon: "📐" },
  { key: "WeightUnit", label: "Weight", icon: "⚖️" },
  { key: "VolumeUnit", label: "Volume", icon: "🧪" },
  { key: "TemperatureUnit", label: "Temp", icon: "🌡️" },
];

const operations = [
  { key: "ADD", label: "Add" },
  { key: "SUBTRACT", label: "Sub" },
  { key: "DIVIDE", label: "Div" },
  { key: "COMPARE", label: "Cmp" },
  { key: "CONVERT", label: "Conv" },
];

function App() {
  // Auto-capture token if backend redirects to frontend with ?token=xxx
  const urlToken = new URLSearchParams(window.location.search).get("token");
  if (urlToken) {
    localStorage.setItem("token", urlToken);
    window.history.replaceState({}, "", window.location.pathname); // clean URL
  }

  const [token, setToken] = useState(urlToken || localStorage.getItem("token") || "");
  const [tokenSaved, setTokenSaved] = useState(!!(urlToken || localStorage.getItem("token")));
  const [showToken, setShowToken] = useState(false);
  const [measurementType, setMeasurementType] = useState("LengthUnit");
  const [operation, setOperation] = useState("add");

  const [value1, setValue1] = useState(1);
  const [unit1, setUnit1] = useState("FEET");

  const [value2, setValue2] = useState(2);
  const [unit2, setUnit2] = useState("FEET");
 const [targetUnit, setTargetUnit] = useState("FEET");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const currentUnits = units[measurementType];

const handleMeasurementTypeChange = (type) => {
  setMeasurementType(type);

  setUnit1(units[type][0]);
  setUnit2(units[type][0]);
  setTargetUnit(units[type][0]);

  // Temperature only supports compare & convert
  if (type === "TemperatureUnit") {
    setOperation("CONVERT");
  } else {
    setOperation("ADD");
  }

  setResult(null);
  setError("");
};

  const login = () => {
    const popup = window.open(
      `${API_BASE}/api/auth/login`,
      "googleLogin",
      "width=500,height=600,left=400,top=100"
    );

    const timer = setInterval(() => {
      try {
        if (!popup || popup.closed) {
          clearInterval(timer);
          return;
        }
        // Try reading the popup's document once it lands on our backend
        const bodyText = popup.document.body?.innerText || "";
        if (bodyText.includes('"token"')) {
          const parsed = JSON.parse(bodyText);
          if (parsed.token) {
            const extractedToken = parsed.token;
            setToken(extractedToken);
            localStorage.setItem("token", extractedToken);
            setTokenSaved(true);
            popup.close();
            clearInterval(timer);
          }
        }
      } catch (e) {
        // Cross-origin — still on Google's page, keep waiting
      }
    }, 500);
  };

  const saveToken = () => {
    localStorage.setItem("token", token);
    setTokenSaved(true);
  };

  const calculate = async () => {
    setLoading(true);
    try {
      setError("");
      setResult(null);

    const payload = {
      thisQuantityDTO: {
        value: parseFloat(value1),
        unit: unit1,
        measurementType,
      },

      thatQuantityDTO: {
        value:
          operation === "CONVERT"
            ? 0
            : parseFloat(value2),

        unit: unit2,
        measurementType,
      },

      // IMPORTANT
      targetUnit: targetUnit,
    };

      const response = await axios.post(
        `${API_BASE}/api/v1/quantities/${operation.toLowerCase()}`,
        payload,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      setResult(response.data);
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data?.error ||
          "Request failed. Check your token and try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      {/* Header */}
      <div className="app-header">
        <div className="app-header-icon">
          <svg viewBox="0 0 24 24">
            <path d="M3 7h18M3 12h18M3 17h12" />
            <circle cx="20" cy="17" r="2" strokeWidth="1.6" />
          </svg>
        </div>
        <div>
          <div className="app-title">Qty Measure</div>
          <div className="app-subtitle">Unit conversion & arithmetic engine</div>
        </div>
      </div>

      {/* Auth Card */}
      <div className="card">
        <div className="card-header">
          <div className="card-badge orange">1</div>
          <div className="card-title">Authentication</div>
        </div>

        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <button className="btn-secondary" onClick={login}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/>
              <polyline points="10 17 15 12 10 7"/>
              <line x1="15" y1="12" x2="3" y2="12"/>
            </svg>
            Login with Google
          </button>
          <button className="btn-ghost" onClick={() => setShowToken(!showToken)} style={{ marginTop: 0 }}>
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
              {showToken
                ? <><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/></>
                : <><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></>
              }
            </svg>
            {showToken ? "Hide Token" : "Paste Token"}
          </button>
        </div>

        <div className="token-status" style={{ marginTop: 10 }}>
          <div className={`token-dot ${tokenSaved && token ? "active" : ""}`} />
          {tokenSaved && token ? "Token saved & ready" : "No token saved"}
        </div>

        {showToken && (
          <>
            <label style={{ marginTop: 16 }}>JWT Token</label>
            <textarea
              placeholder="Paste your JWT token here after login…"
              value={token}
              onChange={(e) => { setToken(e.target.value); setTokenSaved(false); }}
            />
            <div style={{ display: "flex", justifyContent: "flex-end", marginTop: 10 }}>
              <button className="btn-primary" onClick={saveToken} style={{ marginTop: 0 }}>
                Save Token
              </button>
            </div>
          </>
        )}
      </div>

      {/* Calculator Card */}
      <div className="card">
        <div className="card-header">
          <div className="card-badge yellow">2</div>
          <div className="card-title">Calculator</div>
        </div>

        {/* Measurement Type */}
        <label>Measurement Type</label>
        <div className="type-pills">
          {measurementTypes.map((t) => (
            <button
              key={t.key}
              className={`type-pill ${measurementType === t.key ? "active" : ""}`}
              onClick={() => handleMeasurementTypeChange(t.key)}
            >
              {t.icon} {t.label}
            </button>
          ))}
        </div>

        {/* Operation */}
        <label style={{ marginTop: 20 }}>Operation</label>
        <div className="op-pills">
          {operations.map((op) => (
            <button
              key={op.key}
              className={`op-pill ${operation === op.key ? "active" : ""}`}
              onClick={() => { setOperation(op.key); setResult(null); setError(""); }}
            >
              {op.label}
            </button>
          ))}
        </div>

        {/* First Quantity */}
        <div className="quantity-block">
          <div className="quantity-label">First Quantity</div>
          <div className="row-2">
            <div>
              <label>Value</label>
              <input
                type="number"
                value={value1}
                onChange={(e) => setValue1(e.target.value)}
              />
            </div>
            <div>
              <label>Unit</label>
              <div className="select-wrap">
                <select value={unit1} onChange={(e) => setUnit1(e.target.value)}>
                  {currentUnits.map((u) => (
                    <option key={u} value={u}>{u}</option>
                  ))}
                </select>
              </div>
            </div>
          </div>
        </div>

        {/* Second Quantity / Target Unit */}
        <div className="quantity-block">
          <div className="quantity-label">
            {operation === "CONVERT" ? "Target Unit" : "Second Quantity"}
          </div>
          <div className={operation === "CONVERT" ? "" : "row-2"}>
            {operation !== "convert" && (
              <div>
                <label>Value</label>
                <input
                  type="number"
                  value={value2}
                  onChange={(e) => setValue2(e.target.value)}
                />
              </div>
            )}
            <div>
              <label>Unit</label>
              <div className="select-wrap">
                <select value={unit2} onChange={(e) => setUnit2(e.target.value)}>
                  {currentUnits.map((u) => (
                    <option key={u} value={u}>{u}</option>
                  ))}
                </select>
              </div>
            </div>
          </div>
        </div>
        <div className="quantity-block">
          <div className="quantity-label">
            Target Unit
          </div>

          <div>
            <label>Unit</label>

            <div className="select-wrap">
              <select
                value={targetUnit}
                onChange={(e) =>
                  setTargetUnit(e.target.value)
                }
              >
                {currentUnits.map((u) => (
                  <option key={u} value={u}>
                    {u}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        <div className="btn-row">
          <button className="btn-calculate" onClick={calculate} disabled={loading}>
            {loading ? (
              <>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
                  <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83"/>
                </svg>
                Calculating…
              </>
            ) : (
              <>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                  <polyline points="22 7 13.5 15.5 8.5 10.5 2 17"/>
                  <polyline points="16 7 22 7 22 13"/>
                </svg>
                Calculate
              </>
            )}
          </button>
        </div>
      </div>

    {result && (
      <div className="card success">
        <div className="result-label">Result</div>

        <div className="result-value">
          {operation === "COMPARE"
            ? result.value === 0 || result.value === 0.0
              ? "True"
              : "False"
            : result.value}
        </div>

        {operation !== "compare" && result.unit && (
          <div className="result-unit">{result.unit}</div>
        )}
      </div>
    )}
      {/* Error */}
      {error && (
        <div className="card error">
          <div className="error-title">Something went wrong</div>
          <div className="error-msg">{error}</div>
        </div>
      )}

      <div className="footer-note">Quantity Measurement Engine · v1.0</div>
    </div>
  );
}

export default App;