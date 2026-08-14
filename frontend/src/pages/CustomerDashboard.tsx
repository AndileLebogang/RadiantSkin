import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import products from "../data/products";

type ViewId =
  | "dashboard"
  | "products"
  | "cart"
  | "profile"
  | "orders"
  | "reviews";

interface Customer {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
}

interface Product {
  productId: number;
  name: string;
  price: number;
  imageUrl: string;
}

const NAV_ITEMS: { id: ViewId; icon: string; label: string }[] = [
  { id: "dashboard", icon: "", label: "Dashboard" },
  { id: "products", icon: "", label: "Products" },
  { id: "cart", icon: "", label: "Cart" },
  { id: "profile", icon: "", label: "Profile" },
  { id: "orders", icon: "", label: "Orders" },
  { id: "reviews", icon: "", label: "Reviews" },
];

function CustomerDashboard() {
  const [activeView, setActiveView] = useState<ViewId>("dashboard");
  const [rating, setRating] = useState(0);
  const navigate = useNavigate();

  const [customer, setCustomer] = useState<Customer | null>(null);
  const [backendProducts, setBackendProducts] = useState<Product[]>([]);

  useEffect(() => {
    const stored = localStorage.getItem("customer");

    if (!stored) {
      navigate("/login");
      return;
    }

    setCustomer(JSON.parse(stored));

    fetch("http://localhost:8080/product/getAll")
      .then((response) => response.json())
      .then((data) => setBackendProducts(data));
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("customer");
    navigate("/login");
  };

  // Still used by the Cart tab below, which isn't connected to the backend yet
  const allProducts = [
    ...products["skin-care"],
    ...products["body-care"],
    ...products["hair-care"],
  ];

  if (!customer) {
    return null;
  }

  return (
    <div className="admin-page">
      <div className="admin-layout">
        <aside className="sidebar">
          <div className="logo">RadiantSkin</div>
          <div className="role-tag">Customer Account</div>

          <nav>
            {NAV_ITEMS.map((item) => (
              <div
                key={item.id}
                className={`nav-item ${activeView === item.id ? "active" : ""}`}
                onClick={() => setActiveView(item.id)}
              >
                <span className="icon">{item.icon}</span> {item.label}
              </div>
            ))}
          </nav>

          <div className="logout">
            <div className="nav-item" onClick={handleLogout}>
              <span className="icon">↩</span> Logout
            </div>
          </div>
        </aside>

        <main className="main">
          {activeView === "dashboard" && (
            <section>
              <div className="welcome-banner">
                <h2>Welcome back, {customer.firstName}</h2>
                <p>Here's what's happening with your RadiantSkin account.</p>
              </div>

              <div className="panel" style={{ marginBottom: 24 }}>
                <div className="panel-head">
                  <h3>Featured Products</h3>
                </div>
                <div className="product-grid">
                  {backendProducts.slice(0, 4).map((product) => (
                    <div className="product-card card" key={product.productId}>
                      <img src={product.imageUrl} alt={product.name} />
                      <div className="product-info">
                        <h4>{product.name}</h4>
                        <div className="product-price">R{product.price}</div>
                        <div className="product-actions">
                          <Link
                            to={`/product/${product.productId}`}
                            className="btn btn-outline"
                          >
                            View
                          </Link>
                          <button className="btn btn-primary">Add</button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="panel">
                <div className="panel-head">
                  <h3>Recent Orders</h3>
                </div>
                <table>
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Date</th>
                      <th>Amount</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>#RS-1042</td>
                      <td>03 Aug 2026</td>
                      <td>R689</td>
                      <td>
                        <span className="pill pill-success">Delivered</span>
                      </td>
                    </tr>
                    <tr>
                      <td>#RS-1038</td>
                      <td>28 Jul 2026</td>
                      <td>R289</td>
                      <td>
                        <span className="pill pill-primary">Processing</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === "products" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Products</h1>
                  <p>Browse Skin, Body, and Hair Care.</p>
                </div>
              </div>
              <div className="product-grid">
                {backendProducts.map((product) => (
                  <div className="product-card card" key={product.productId}>
                    <img src={product.imageUrl} alt={product.name} />
                    <div className="product-info">
                      <h4>{product.name}</h4>
                      <div className="product-price">R{product.price}</div>
                      <div className="product-actions">
                        <Link
                          to={`/product/${product.productId}`}
                          className="btn btn-outline"
                        >
                          View Details
                        </Link>
                        <button className="btn btn-primary">Add to Cart</button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </section>
          )}

          {activeView === "cart" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Your Cart</h1>
                  <p>Review items before checking out.</p>
                </div>
              </div>

              <div className="panel" style={{ marginBottom: 24 }}>
                <div className="cart-item">
                  <img src={products["body-care"][0].img} alt="" />
                  <div className="cart-item-info">
                    <h4>Whipped Shea Body Butter</h4>
                    <div className="product-price">R289</div>
                  </div>
                  <div className="qty-control">
                    <button>−</button>
                    <span>1</span>
                    <button>+</button>
                  </div>
                  <button className="icon-btn">🗑</button>
                </div>
                <div className="cart-item">
                  <img src={products["hair-care"][0].img} alt="" />
                  <div className="cart-item-info">
                    <h4>Keratin Repair Shampoo</h4>
                    <div className="product-price">R219</div>
                  </div>
                  <div className="qty-control">
                    <button>−</button>
                    <span>2</span>
                    <button>+</button>
                  </div>
                  <button className="icon-btn">🗑</button>
                </div>
              </div>

              <div className="panel">
                <div className="cart-summary">
                  <span>Subtotal</span>
                  <span>R727</span>
                </div>
                <div className="cart-summary">
                  <span>Delivery</span>
                  <span>R60</span>
                </div>
                <div className="cart-summary total">
                  <span>Total</span>
                  <span>R787</span>
                </div>
                <button
                  className="btn btn-primary btn-block"
                  style={{ marginTop: 18 }}
                >
                  Checkout
                </button>
              </div>
            </section>
          )}

          {activeView === "profile" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Profile</h1>
                  <p>Update your personal information.</p>
                </div>
              </div>

              <div className="panel">
                <div className="profile-grid">
                  <div className="form-group">
                    <label>First Name</label>
                    <input type="text" defaultValue={customer.firstName} />
                  </div>
                  <div className="form-group">
                    <label>Last Name</label>
                    <input type="text" defaultValue={customer.lastName} />
                  </div>
                  <div className="form-group">
                    <label>Email Address</label>
                    <input type="email" defaultValue={customer.email} />
                  </div>
                  <div className="form-group">
                    <label>Phone Number</label>
                    <input type="tel" defaultValue={customer.phoneNumber} />
                  </div>
                  <div className="form-group" style={{ gridColumn: "1 / -1" }}>
                    <label>Address</label>
                    <input type="text" placeholder="Not set yet" />
                  </div>
                </div>
                <button className="btn btn-primary" style={{ marginTop: 10 }}>
                  Save Changes
                </button>
              </div>
            </section>
          )}

          {activeView === "orders" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Orders</h1>
                  <p>Your order history and details.</p>
                </div>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Date</th>
                      <th>Items</th>
                      <th>Amount</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>#RS-1042</td>
                      <td>03 Aug 2026</td>
                      <td>3</td>
                      <td>R689</td>
                      <td>
                        <span className="pill pill-success">Delivered</span>
                      </td>
                    </tr>
                    <tr>
                      <td>#RS-1038</td>
                      <td>28 Jul 2026</td>
                      <td>1</td>
                      <td>R289</td>
                      <td>
                        <span className="pill pill-primary">Processing</span>
                      </td>
                    </tr>
                    <tr>
                      <td>#RS-1021</td>
                      <td>14 Jun 2026</td>
                      <td>2</td>
                      <td>R448</td>
                      <td>
                        <span className="pill pill-success">Delivered</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === "reviews" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Leave a Review</h1>
                  <p>Share your experience with a product you've purchased.</p>
                </div>
              </div>

              <div className="panel">
                <div className="form-group">
                  <label>Product</label>
                  <input type="text" defaultValue="Whipped Shea Body Butter" />
                </div>

                <label>Your Rating</label>
                <div className="star-input">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <span
                      key={star}
                      className={star <= rating ? "filled" : ""}
                      onClick={() => setRating(star)}
                    >
                      ★
                    </span>
                  ))}
                </div>

                <div className="form-group">
                  <label>Comment</label>
                  <textarea placeholder="Tell us what you thought..." />
                </div>

                <button className="btn btn-primary">Submit Review</button>
              </div>
            </section>
          )}
        </main>
      </div>
    </div>
  );
}

export default CustomerDashboard;
