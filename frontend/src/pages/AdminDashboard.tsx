import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

type ViewId =
  | "dashboard"
  | "products"
  | "customers"
  | "orders"
  | "payments"
  | "reviews";

const NAV_ITEMS: { id: ViewId; label: string }[] = [
  { id: "dashboard", label: "Dashboard" },
  { id: "products", label: "Products" },
  { id: "customers", label: "Customers" },
  { id: "orders", label: "Orders" },
  { id: "payments", label: "Payments" },
  { id: "reviews", label: "Reviews" },
];

interface ProductData {
  productId: number;
  name: string;
  price: number;
  stockQuantity: number;
  category: string;
}

interface CustomerData {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
}

interface ReviewData {
  reviewId: number;
  rating: number;
  comment: string;
  customer: { firstName: string; lastName: string };
  product: { name: string };
}

interface OrderData {
  orderId: number;
  orderDate: string;
  status: string;
  totalAmount: number;
  customer: { firstName: string; lastName: string };
  orderItems: unknown[];
}

function AdminDashboard() {
  const [activeView, setActiveView] = useState<ViewId>("dashboard");
  const navigate = useNavigate();

  const [products, setProducts] = useState<ProductData[]>([]);
  const [customers, setCustomers] = useState<CustomerData[]>([]);
  const [reviews, setReviews] = useState<ReviewData[]>([]);
  const [orders, setOrders] = useState<OrderData[]>([]);
  const [stats, setStats] = useState({
    totalCustomers: 0,
    totalProducts: 0,
    totalReviews: 0,
  });

  // Fields for the "Add Product" form
  const [newCategory, setNewCategory] = useState("skincare");
  const [newName, setNewName] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newBrand, setNewBrand] = useState("");
  const [newPrice, setNewPrice] = useState("");
  const [newStock, setNewStock] = useState("");
  const [newImageUrl, setNewImageUrl] = useState("");
  const [newVolumeMl, setNewVolumeMl] = useState("");
  const [newExtraInfo, setNewExtraInfo] = useState("");

  function loadProducts() {
    Promise.all([
      fetch("http://localhost:8080/skincare/getAll").then((r) => r.json()),
      fetch("http://localhost:8080/bodycare/getAll").then((r) => r.json()),
      fetch("http://localhost:8080/haircare/getAll").then((r) => r.json()),
    ]).then(function (results) {
      const skinProducts = results[0].map(function (p: ProductData) {
        return {
          productId: p.productId,
          name: p.name,
          price: p.price,
          stockQuantity: p.stockQuantity,
          category: "Skin Care",
        };
      });
      const bodyProducts = results[1].map(function (p: ProductData) {
        return {
          productId: p.productId,
          name: p.name,
          price: p.price,
          stockQuantity: p.stockQuantity,
          category: "Body Care",
        };
      });
      const hairProducts = results[2].map(function (p: ProductData) {
        return {
          productId: p.productId,
          name: p.name,
          price: p.price,
          stockQuantity: p.stockQuantity,
          category: "Hair Care",
        };
      });

      setProducts(skinProducts.concat(bodyProducts, hairProducts));
    });
  }

  function loadCustomers() {
    fetch("http://localhost:8080/customer/getAll")
      .then((r) => r.json())
      .then((data) => setCustomers(data));
  }

  function loadReviews() {
    fetch("http://localhost:8080/review/getAll")
      .then((r) => r.json())
      .then((data) => setReviews(data));
  }

  function loadOrders() {
    fetch("http://localhost:8080/order/getAll")
      .then((r) => r.json())
      .then((data) => setOrders(data));
  }

  function loadStats() {
    fetch("http://localhost:8080/dashboard/stats")
      .then((r) => r.json())
      .then((data) => setStats(data));
  }

  useEffect(function () {
    loadProducts();
    loadCustomers();
    loadReviews();
    loadOrders();
    loadStats();
  }, []);

  function getEndpointForCategory(category: string) {
    if (category === "Skin Care") return "skincare";
    if (category === "Body Care") return "bodycare";
    return "haircare";
  }

  function handleDeleteProduct(product: ProductData) {
    const endpoint = getEndpointForCategory(product.category);

    fetch(
      "http://localhost:8080/" + endpoint + "/delete/" + product.productId,
      {
        method: "DELETE",
      },
    ).then(function () {
      loadProducts();
      loadStats();
    });
  }

  function handleDeleteCustomer(customerId: number) {
    fetch("http://localhost:8080/customer/delete/" + customerId, {
      method: "DELETE",
    }).then(function () {
      loadCustomers();
      loadStats();
    });
  }

  function handleDeleteReview(reviewId: number) {
    fetch("http://localhost:8080/review/delete/" + reviewId, {
      method: "DELETE",
    }).then(function () {
      loadReviews();
      loadStats();
    });
  }

  // Change an order's status when the admin picks a new option from the dropdown
  function handleStatusChange(orderId: number, newStatus: string) {
    fetch("http://localhost:8080/order/updateStatus", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ orderId: orderId, status: newStatus }),
    }).then(function () {
      loadOrders();
    });
  }

  function handleAddProduct(e: React.FormEvent) {
    e.preventDefault();

    const endpoint = newCategory;

    const productData: Record<string, string | number> = {
      name: newName,
      description: newDescription,
      brand: newBrand,
      price: parseFloat(newPrice),
      stockQuantity: parseInt(newStock),
      imageUrl: newImageUrl,
      volumeMl: parseInt(newVolumeMl),
    };

    if (newCategory === "skincare") {
      productData.usageInstructions = newExtraInfo;
    } else if (newCategory === "bodycare") {
      productData.skinConcern = newExtraInfo;
    } else {
      productData.hairConcern = newExtraInfo;
    }

    fetch("http://localhost:8080/" + endpoint + "/create", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(productData),
    }).then(function () {
      setNewName("");
      setNewDescription("");
      setNewBrand("");
      setNewPrice("");
      setNewStock("");
      setNewImageUrl("");
      setNewVolumeMl("");
      setNewExtraInfo("");

      loadProducts();
      loadStats();
    });
  }

  function handleLogout() {
    localStorage.removeItem("customer");
    navigate("/login");
  }

  return (
    <div className="admin-page">
      <div className="admin-layout">
        <aside className="sidebar">
          <div className="logo">RadiantSkin</div>
          <div className="role-tag">Admin Panel</div>

          <nav>
            {NAV_ITEMS.map(function (item) {
              return (
                <div
                  key={item.id}
                  className={
                    "nav-item " + (activeView === item.id ? "active" : "")
                  }
                  onClick={() => setActiveView(item.id)}
                >
                  {item.label}
                </div>
              );
            })}
          </nav>

          <div className="logout">
            <div className="nav-item" onClick={handleLogout}>
              Logout
            </div>
          </div>
        </aside>

        <main className="main">
          {activeView === "dashboard" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Dashboard Overview</h1>
                  <p>Welcome back, here's what's happening today.</p>
                </div>
              </div>

              <div className="stat-grid">
                <div className="stat-card">
                  <div className="stat-label">Total Products</div>
                  <div className="stat-value">{stats.totalProducts}</div>
                </div>
                <div className="stat-card">
                  <div className="stat-label">Total Customers</div>
                  <div className="stat-value">{stats.totalCustomers}</div>
                </div>
                <div className="stat-card">
                  <div className="stat-label">Total Reviews</div>
                  <div className="stat-value">{stats.totalReviews}</div>
                </div>
                <div className="stat-card">
                  <div className="stat-label">Total Orders</div>
                  <div className="stat-value">{orders.length}</div>
                </div>
              </div>

              <div className="panel">
                <div className="panel-head">
                  <h3>Recent Orders</h3>
                </div>

                {orders.length === 0 && (
                  <p style={{ color: "#6b7280" }}>No orders yet.</p>
                )}

                {orders.length > 0 && (
                  <table>
                    <thead>
                      <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Date</th>
                        <th>Amount</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {orders.slice(0, 5).map(function (order) {
                        return (
                          <tr key={order.orderId}>
                            <td>#{order.orderId}</td>
                            <td>
                              {order.customer.firstName}{" "}
                              {order.customer.lastName}
                            </td>
                            <td>
                              {new Date(order.orderDate).toLocaleDateString()}
                            </td>
                            <td>R{order.totalAmount}</td>
                            <td>{order.status}</td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                )}
              </div>
            </section>
          )}

          {activeView === "products" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Products</h1>
                  <p>Manage your Skin, Body, and Hair Care catalogue.</p>
                </div>
              </div>

              <div className="panel" style={{ marginBottom: 24 }}>
                <div className="panel-head">
                  <h3>Add New Product</h3>
                </div>

                <form onSubmit={handleAddProduct}>
                  <div className="form-group">
                    <label>Category</label>
                    <select
                      value={newCategory}
                      onChange={(e) => setNewCategory(e.target.value)}
                    >
                      <option value="skincare">Skin Care</option>
                      <option value="bodycare">Body Care</option>
                      <option value="haircare">Hair Care</option>
                    </select>
                  </div>

                  <div className="form-group">
                    <label>Name</label>
                    <input
                      value={newName}
                      onChange={(e) => setNewName(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Description</label>
                    <input
                      value={newDescription}
                      onChange={(e) => setNewDescription(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Brand</label>
                    <input
                      value={newBrand}
                      onChange={(e) => setNewBrand(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Price (R)</label>
                    <input
                      type="number"
                      value={newPrice}
                      onChange={(e) => setNewPrice(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Stock Quantity</label>
                    <input
                      type="number"
                      value={newStock}
                      onChange={(e) => setNewStock(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Volume (ml)</label>
                    <input
                      type="number"
                      value={newVolumeMl}
                      onChange={(e) => setNewVolumeMl(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Image URL</label>
                    <input
                      value={newImageUrl}
                      onChange={(e) => setNewImageUrl(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Extra Info (usage instructions / concern)</label>
                    <input
                      value={newExtraInfo}
                      onChange={(e) => setNewExtraInfo(e.target.value)}
                      required
                    />
                  </div>

                  <button type="submit" className="btn btn-primary">
                    Add Product
                  </button>
                </form>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr>
                      <th>Product</th>
                      <th>Category</th>
                      <th>Price</th>
                      <th>Stock</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {products.map(function (product) {
                      return (
                        <tr key={product.productId}>
                          <td>{product.name}</td>
                          <td>{product.category}</td>
                          <td>R{product.price}</td>
                          <td>{product.stockQuantity}</td>
                          <td className="table-actions">
                            <button
                              className="icon-btn"
                              onClick={() => handleDeleteProduct(product)}
                            >
                              🗑
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === "customers" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Customers</h1>
                  <p>View and manage registered customer accounts.</p>
                </div>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {customers.map(function (customer) {
                      return (
                        <tr key={customer.userId}>
                          <td>
                            {customer.firstName} {customer.lastName}
                          </td>
                          <td>{customer.email}</td>
                          <td className="table-actions">
                            <button
                              className="icon-btn"
                              onClick={() =>
                                handleDeleteCustomer(customer.userId)
                              }
                            >
                              🗑
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === "orders" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Orders</h1>
                  <p>Track and update the status of all customer orders.</p>
                </div>
              </div>

              {orders.length === 0 && (
                <div className="panel">
                  <p style={{ color: "#6b7280" }}>No orders yet.</p>
                </div>
              )}

              {orders.length > 0 && (
                <div className="panel">
                  <table>
                    <thead>
                      <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Items</th>
                        <th>Amount</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {orders.map(function (order) {
                        return (
                          <tr key={order.orderId}>
                            <td>#{order.orderId}</td>
                            <td>
                              {order.customer.firstName}{" "}
                              {order.customer.lastName}
                            </td>
                            <td>{order.orderItems.length}</td>
                            <td>R{order.totalAmount}</td>
                            <td>
                              <select
                                value={order.status}
                                onChange={(e) =>
                                  handleStatusChange(
                                    order.orderId,
                                    e.target.value,
                                  )
                                }
                              >
                                <option value="PENDING">Pending</option>
                                <option value="PROCESSING">Processing</option>
                                <option value="SHIPPED">Shipped</option>
                                <option value="DELIVERED">Delivered</option>
                                <option value="CANCELLED">Cancelled</option>
                              </select>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              )}
            </section>
          )}

          {activeView === "payments" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Payments</h1>
                  <p>Review payment transactions and their status.</p>
                </div>
              </div>
              <div className="panel">
                <p style={{ color: "#6b7280" }}>
                  Payment tracking is not connected yet — coming in the next
                  phase.
                </p>
              </div>
            </section>
          )}

          {activeView === "reviews" && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Reviews</h1>
                  <p>Moderate customer reviews across all products.</p>
                </div>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr>
                      <th>Customer</th>
                      <th>Product</th>
                      <th>Rating</th>
                      <th>Comment</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {reviews.map(function (review) {
                      return (
                        <tr key={review.reviewId}>
                          <td>
                            {review.customer.firstName}{" "}
                            {review.customer.lastName}
                          </td>
                          <td>{review.product.name}</td>
                          <td>{review.rating} / 5</td>
                          <td>{review.comment}</td>
                          <td className="table-actions">
                            <button
                              className="icon-btn"
                              onClick={() =>
                                handleDeleteReview(review.reviewId)
                              }
                            >
                              🗑
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </section>
          )}
        </main>
      </div>
    </div>
  );
}

export default AdminDashboard;
