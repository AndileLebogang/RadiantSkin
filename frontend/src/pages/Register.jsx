import "./Register.css";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";

function Register() {
  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    password: "",
  });
  const [confirmPassword, setConfirmPassword] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (formData.password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/customer/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        navigate("/login");
      } else {
        const message = await response.text();
        setError(message || "Registration failed. Please try again.");
      }
    } catch (err) {
      setError("Could not reach the server. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      {/* Left Section */}
      <div className="register-left">
        <div className="overlay">
          <h1>
            Radiant<span>Skin</span>
          </h1>

          <h2>Glow with Confidence.</h2>

          <p>
            Join RadiantSkin today and discover premium skincare products
            specially selected to nourish, protect and enhance your natural
            beauty.
          </p>

          <div className="feature-list">
            <div className="feature">
              <span>✔</span>
              <p>Premium Quality Products</p>
            </div>

            <div className="feature">
              <span>✔</span>
              <p>Secure Online Shopping</p>
            </div>

            <div className="feature">
              <span>✔</span>
              <p>Fast Delivery Across South Africa</p>
            </div>
          </div>
        </div>
      </div>

      {/* Right Section */}
      <div className="register-right">
        <div className="register-card">
          <h2>Create Your Account</h2>

          <p className="subtitle">
            Fill in your details below to create your RadiantSkin account.
          </p>

          <form onSubmit={handleSubmit}>
            <div className="row">
              <div className="input-group">
                <label>First Name</label>
                <input
                  type="text"
                  name="firstName"
                  placeholder="Enter your first name"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="input-group">
                <label>Last Name</label>
                <input
                  type="text"
                  name="lastName"
                  placeholder="Enter your last name"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

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
              <label>Phone Number</label>
              <input
                type="tel"
                name="phoneNumber"
                placeholder="Enter your phone number"
                value={formData.phoneNumber}
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
                  placeholder="Create a password"
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

            <div className="input-group">
              <label>Confirm Password</label>

              <div className="password-input">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  placeholder="Confirm your password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  required
                />

                <button
                  type="button"
                  className="show-password"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                >
                  {showConfirmPassword ? "Hide" : "Show"}
                </button>
              </div>
            </div>

            <div className="checkbox">
              <input type="checkbox" id="terms" required />

              <label htmlFor="terms">
                I agree to the Terms & Conditions and Privacy Policy.
              </label>
            </div>

            {error && <p className="error-message">{error}</p>}

            <button className="register-btn" type="submit" disabled={loading}>
              {loading ? "Creating Account..." : "Create Account"}
            </button>
          </form>

          <div className="bottom-links">
            <p>
              Already have an account? <Link to="/login">Sign In</Link>
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

export default Register;
