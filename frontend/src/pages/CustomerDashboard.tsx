import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

type ViewId =
  | "dashboard"
  | "products"
  | "cart"
  | "profile"
  | "orders"
  | "reviews";

const NAV_ITEMS: { id: ViewId; label: string }[] = [
  { id: "dashboard", label: "Dashboard" },
  { id: "products", label: "Products" },
  { id: "cart", label: "Cart" },
  { id: "profile", label: "Profile" },
  { id: "orders", label: "Orders" },
  { id: "reviews", label: "Reviews" },
];

interface Customer {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
}

interface ProductData {
  productId: number;
  name: string;
  price: number;
  imageUrl: string;
}

interface CartItemData {
  cartItemId: number;
  quantity: number;
  product: {
    productId: number;
    name: string;
    price: number;
    imageUrl: string;
  };
}

interface OrderData {
  orderId: number;
  orderDate: string;
  status: string;
  totalAmount: number;
  orderItems: unknown[];
}

const DELIVERY_FEE = 60;

function CustomerDashboard() {
  const navigate = useNavigate();
  const [activeView, setActiveView] = useState<ViewId>("dashboard");

  const [customer] = useState<Customer | null>(() => {
    try {
      const stored = localStorage.getItem("customer");
      return stored ? JSON.parse(stored) as Customer : null;
    } catch { return null; }
  });
  const [products, setProducts] = useState<ProductData[]>([]);
  const [cartItems, setCartItems] = useState<CartItemData[]>([]);
  const [orders, setOrders] = useState<OrderData[]>([]);
  const [addToCartMessage, setAddToCartMessage] = useState("");
  const [checkoutMessage, setCheckoutMessage] = useState("");

  // Address form fields
  const [street, setStreet] = useState("");
  const [city, setCity] = useState("");
  const [province, setProvince] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [country, setCountry] = useState("");
  const [addressMessage, setAddressMessage] = useState("");

  // Review form fields
  const [reviewProductId, setReviewProductId] = useState("");
  const [reviewRating, setReviewRating] = useState(5);
  const [reviewComment, setReviewComment] = useState("");
  const [reviewMessage, setReviewMessage] = useState("");

  useEffect(() => {
    if (!customer) navigate("/login");
  }, [customer, navigate]);

  // Get all products from the three categories, combined into one list
  function loadProducts() {
    Promise.all([
      fetch("http://localhost:8080/skincare/getAll").then((r) => r.json()),
      fetch("http://localhost:8080/bodycare/getAll").then((r) => r.json()),
      fetch("http://localhost:8080/haircare/getAll").then((r) => r.json()),
    ]).then(function (results) {
      setProducts(results[0].concat(results[1], results[2]));
    });
  }

  // Get the customer's real cart from the backend
  function loadCart(customerId: number) {
    fetch("http://localhost:8080/cart/customer/" + customerId)
      .then((r) => r.json())
      .then(function (data) {
        setCartItems(data && data.cartItems ? data.cartItems : []);
      });
  }

  // Get the customer's real order history from the backend
  function loadOrders(customerId: number) {
    fetch("http://localhost:8080/order/customer/" + customerId)
      .then((r) => r.json())
      .then((data) => setOrders(data));
  }

  // Load everything once we know who the customer is
  useEffect(
    function () {
      if (customer) {
        loadProducts();
        loadCart(customer.userId);
        loadOrders(customer.userId);
      }
    },
    [customer],
  );

  // Add a product to the cart
  function handleAddToCart(productId: number) {
    if (!customer) return;

    fetch("http://localhost:8080/cartitem/add", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        customerId: customer.userId,
        productId: productId,
        quantity: 1,
      }),
    }).then(function () {
      setAddToCartMessage("Added to cart!");
      loadCart(customer.userId);
      setTimeout(function () {
        setAddToCartMessage("");
      }, 2000);
    });
  }

  function handleIncrease(item: CartItemData) {
    if (!customer) return;
    fetch("http://localhost:8080/cartitem/updateQuantity", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        cartItemId: item.cartItemId,
        quantity: item.quantity + 1,
      }),
    }).then(() => loadCart(customer.userId));
  }

  function handleDecrease(item: CartItemData) {
    if (!customer) return;

    if (item.quantity <= 1) {
      handleRemoveFromCart(item.cartItemId);
      return;
    }

    fetch("http://localhost:8080/cartitem/updateQuantity", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        cartItemId: item.cartItemId,
        quantity: item.quantity - 1,
      }),
    }).then(() => loadCart(customer.userId));
  }

  function handleRemoveFromCart(cartItemId: number) {
    if (!customer) return;
    fetch("http://localhost:8080/cartitem/delete/" + cartItemId, {
      method: "DELETE",
    }).then(() => loadCart(customer.userId));
  }

  // Turn the cart into a real order, then go to the confirmation page
  function handleCheckout() {
    if (!customer) return;

    fetch("http://localhost:8080/order/checkout/" + customer.userId, {
      method: "POST",
    }).then(function (response) {
      if (response.ok) {
        response.json().then(function (order) {
          loadCart(customer.userId);
          loadOrders(customer.userId);
          navigate("/order-confirmation", { state: order });
        });
      } else {
        response.text().then((msg) => setCheckoutMessage(msg));
        setTimeout(function () {
          setCheckoutMessage("");
        }, 2500);
      }
    });
  }

  // Save the customer's address (creates it the first time, updates it after that)
  function handleSaveAddress(e: React.FormEvent) {
    e.preventDefault();
    if (!customer) return;

    fetch("http://localhost:8080/address/save", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        customerId: customer.userId,
        street: street,
        city: city,
        province: province,
        postalCode: postalCode,
        country: country,
      }),
    }).then(function () {
      setAddressMessage("Address saved!");
      setTimeout(function () {
        setAddressMessage("");
      }, 2000);
    });
  }

  // Submit a review for a product
  function handleSubmitReview(e: React.FormEvent) {
    e.preventDefault();
    if (!customer) return;

    fetch("http://localhost:8080/review/add", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        customerId: customer.userId,
        productId: parseInt(reviewProductId),
        rating: reviewRating,
        comment: reviewComment,
      }),
    }).then(function (response) {
      if (response.ok) {
        setReviewMessage("Review submitted, thank you!");
        setReviewComment("");
      } else {
        setReviewMessage("Something went wrong, please try again.");
      }
      setTimeout(function () {
        setReviewMessage("");
      }, 2500);
    });
  }

  function handleLogout() {
    localStorage.removeItem("customer");
    navigate("/login");
  }

  // Work out cart totals
  let subtotal = 0;
  for (let i = 0; i < cartItems.length; i++) {
    subtotal = subtotal + cartItems[i].product.price * cartItems[i].quantity;
  }
  const delivery = cartItems.length > 0 ? DELIVERY_FEE : 0;
  const total = subtotal + delivery;

  // Don't show anything until we know who the customer is
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
              <div className="welcome-banner">
                <h2>Welcome back, {customer.firstName}</h2>
                <p>Here's what's happening with your RadiantSkin account.</p>
              </div>

              <div className="panel">
                <div className="panel-head">
                  <h3>Recent Orders</h3>
                </div>

                {orders.length === 0 && (
                  <p style={{ color: "#6b7280" }}>You have no orders yet.</p>
                )}

                {orders.length > 0 && (
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
                      {orders.slice(0, 3).map(function (order) {
                        return (
                          <tr key={order.orderId}>
                            <td>#{order.orderId}</td>
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
                  <p>Browse Skin, Body, and Hair Care.</p>
                </div>
              </div>

              {addToCartMessage && (
                <p style={{ color: "green" }}>{addToCartMessage}</p>
              )}

              <div className="product-grid">
                {products.map(function (product) {
                  return (
                    <div className="product-card card" key={product.productId}>
                      <img src={product.imageUrl} alt={product.name} />
                      <div className="product-info">
                        <h4>{product.name}</h4>
                        <div className="product-price">R{product.price}</div>
                        <div className="product-actions">
                          <button
                            className="btn btn-primary"
                            onClick={() => handleAddToCart(product.productId)}
                          >
                            Add to Cart
                          </button>
                        </div>
                      </div>
                    </div>
                  );
                })}
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

              {cartItems.length === 0 && (
                <div className="panel">
                  <p>
                    Your cart is empty. Browse products to add something you'll
                    love.
                  </p>
                </div>
              )}

              {cartItems.length > 0 && (
                <>
                  <div className="panel" style={{ marginBottom: 24 }}>
                    {cartItems.map(function (item) {
                      return (
                        <div className="cart-item" key={item.cartItemId}>
                          <img
                            src={item.product.imageUrl}
                            alt={item.product.name}
                          />
                          <div className="cart-item-info">
                            <h4>{item.product.name}</h4>
                            <div className="product-price">
                              R{item.product.price}
                            </div>
                          </div>
                          <div className="qty-control">
                            <button onClick={() => handleDecrease(item)}>
                              -
                            </button>
                            <span>{item.quantity}</span>
                            <button onClick={() => handleIncrease(item)}>
                              +
                            </button>
                          </div>
                          <button
                            className="icon-btn"
                            onClick={() =>
                              handleRemoveFromCart(item.cartItemId)
                            }
                          >
                            🗑
                          </button>
                        </div>
                      );
                    })}
                  </div>

                  <div className="panel">
                    <div className="cart-summary">
                      <span>Subtotal</span>
                      <span>R{subtotal}</span>
                    </div>
                    <div className="cart-summary">
                      <span>Delivery</span>
                      <span>R{delivery}</span>
                    </div>
                    <div className="cart-summary total">
                      <span>Total</span>
                      <span>R{total}</span>
                    </div>

                    {checkoutMessage && (
                      <p style={{ color: "green" }}>{checkoutMessage}</p>
                    )}

                    <button
                      className="btn btn-primary btn-block"
                      style={{ marginTop: 18 }}
                      onClick={handleCheckout}
                    >
                      Checkout
                    </button>
                  </div>
                </>
              )}
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

              <div className="panel" style={{ marginBottom: 24 }}>
                <div className="profile-grid">
                  <div className="form-group">
                    <label>First Name</label>
                    <input type="text" value={customer.firstName} disabled />
                  </div>
                  <div className="form-group">
                    <label>Last Name</label>
                    <input type="text" value={customer.lastName} disabled />
                  </div>
                  <div className="form-group">
                    <label>Email Address</label>
                    <input type="email" value={customer.email} disabled />
                  </div>
                  <div className="form-group">
                    <label>Phone Number</label>
                    <input type="tel" value={customer.phoneNumber} disabled />
                  </div>
                </div>
              </div>

              <div className="panel">
                <div className="panel-head">
                  <h3>Delivery Address</h3>
                </div>

                <form onSubmit={handleSaveAddress}>
                  <div className="profile-grid">
                    <div className="form-group">
                      <label>Street</label>
                      <input
                        value={street}
                        onChange={(e) => setStreet(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label>City</label>
                      <input
                        value={city}
                        onChange={(e) => setCity(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label>Province</label>
                      <input
                        value={province}
                        onChange={(e) => setProvince(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label>Postal Code</label>
                      <input
                        value={postalCode}
                        onChange={(e) => setPostalCode(e.target.value)}
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label>Country</label>
                      <input
                        value={country}
                        onChange={(e) => setCountry(e.target.value)}
                        required
                      />
                    </div>
                  </div>

                  {addressMessage && (
                    <p style={{ color: "green" }}>{addressMessage}</p>
                  )}

                  <button
                    type="submit"
                    className="btn btn-primary"
                    style={{ marginTop: 10 }}
                  >
                    Save Address
                  </button>
                </form>
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

              {orders.length === 0 && (
                <div className="panel">
                  <p style={{ color: "#6b7280" }}>You have no orders yet.</p>
                </div>
              )}

              {orders.length > 0 && (
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
                      {orders.map(function (order) {
                        return (
                          <tr key={order.orderId}>
                            <td>#{order.orderId}</td>
                            <td>
                              {new Date(order.orderDate).toLocaleDateString()}
                            </td>
                            <td>{order.orderItems.length}</td>
                            <td>R{order.totalAmount}</td>
                            <td>{order.status}</td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              )}
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
                <form onSubmit={handleSubmitReview}>
                  <div className="form-group">
                    <label>Product</label>
                    <select
                      value={reviewProductId}
                      onChange={(e) => setReviewProductId(e.target.value)}
                      required
                    >
                      <option value="">-- Choose a product --</option>
                      {products.map(function (product) {
                        return (
                          <option
                            key={product.productId}
                            value={product.productId}
                          >
                            {product.name}
                          </option>
                        );
                      })}
                    </select>
                  </div>

                  <label>Your Rating</label>
                  <div className="star-input">
                    {[1, 2, 3, 4, 5].map(function (star) {
                      return (
                        <span
                          key={star}
                          className={star <= reviewRating ? "filled" : ""}
                          onClick={() => setReviewRating(star)}
                        >
                          ★
                        </span>
                      );
                    })}
                  </div>

                  <div className="form-group">
                    <label>Comment</label>
                    <textarea
                      placeholder="Tell us what you thought..."
                      value={reviewComment}
                      onChange={(e) => setReviewComment(e.target.value)}
                      required
                    />
                  </div>

                  {reviewMessage && (
                    <p style={{ color: "green" }}>{reviewMessage}</p>
                  )}

                  <button type="submit" className="btn btn-primary">
                    Submit Review
                  </button>
                </form>
              </div>
            </section>
          )}
        </main>
      </div>
    </div>
  );
}

export default CustomerDashboard;
