import "./Login.css";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";

function Login() {
  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/customer/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const customer = await response.json();
        // Store basic session info so the app knows who's logged in
        localStorage.setItem("customer", JSON.stringify(customer));
        navigate("/dashboard");
      } else {
        const message = await response.text();
        setError(message || "Invalid email or password.");
      }
    } catch (err) {
      setError("Could not reach the server. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      {/* Left Section */}
      <div className="login-left">
        <div className="overlay">
          <h1>
            Radiant<span>Skin</span>
          </h1>

          <h2>Welcome Back.</h2>

          <p>
            Sign in to continue your skincare journey and pick up right where
            you left off.
          </p>

          <div className="feature-list">
            <div className="feature">
              <span>✔</span>
              <p>Track Your Orders</p>
            </div>

            <div className="feature">
              <span>✔</span>
              <p>Manage Your Cart</p>
            </div>

            <div className="feature">
              <span>✔</span>
              <p>Exclusive Member Offers</p>
            </div>
          </div>
        </div>
      </div>

      {/* Right Section */}
      <div className="login-right">
        <div className="login-card">
          <h2>Sign In</h2>

          <p className="subtitle">
            Enter your details below to access your RadiantSkin account.
          </p>

          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <label>Email Address</label>
              <input
                type="email"
                name="email"
                placeholder="Enter your email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="input-group">
              <label>Password</label>

              <div className="password-input">
                <input
                  type={showPassword ? "text" : "password"}
                  name="password"
                  placeholder="Enter your password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />

                <button
                  type="button"
                  className="show-password"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? "Hide" : "Show"}
                </button>
              </div>
            </div>

            {error && <p className="error-message">{error}</p>}

            <button className="login-btn" type="submit" disabled={loading}>
              {loading ? "Signing In..." : "Sign In"}
            </button>
          </form>

          <div className="bottom-links">
            <p>
              Don't have an account? <Link to="/register">Create Account</Link>
            </p>

            <Link to="/" className="back-home">
              ← Back to Home
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
