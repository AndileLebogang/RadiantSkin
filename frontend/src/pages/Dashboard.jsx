import "./Dashboard.css";
import { useNavigate, Link } from "react-router-dom";
import { useEffect, useState } from "react";

function Dashboard() {
  const navigate = useNavigate();
  const [customer, setCustomer] = useState(null);

  useEffect(() => {
    const stored = localStorage.getItem("customer");

    if (!stored) {
      // Not logged in — send them to login instead of showing the dashboard
      navigate("/login");
      return;
    }

    setCustomer(JSON.parse(stored));
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("customer");
    navigate("/");
  };

  // Avoid flashing an empty dashboard while we check localStorage
  if (!customer) {
    return null;
  }

  return (
    <div className="dashboard-page">
      <nav className="dashboard-navbar">
        <div className="logo">
          <h2>
            Radiant<span>Skin</span>
          </h2>
        </div>

        <button className="logout-button" onClick={handleLogout}>
          Logout
        </button>
      </nav>

      <div className="dashboard-content">
        <div className="welcome-section">
          <h1>Welcome back, {customer.firstName} 👋</h1>
          <p>{customer.email}</p>
        </div>

        <div className="dashboard-grid">
          <Link to="/products" className="dashboard-card">
            <div className="card-icon">🛍️</div>
            <h3>Browse Products</h3>
            <p>Explore our skincare collection.</p>
          </Link>

          <Link to="/cart" className="dashboard-card">
            <div className="card-icon">🛒</div>
            <h3>My Cart</h3>
            <p>View and manage items in your cart.</p>
          </Link>

          <Link to="/orders" className="dashboard-card">
            <div className="card-icon">📦</div>
            <h3>My Orders</h3>
            <p>Track your past and current orders.</p>
          </Link>

          <Link to="/profile" className="dashboard-card">
            <div className="card-icon">👤</div>
            <h3>My Profile</h3>
            <p>View and update your account details.</p>
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
