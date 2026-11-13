import { Link, useLocation, useNavigate } from "react-router-dom";

function OrderConfirmation() {
  const location = useLocation();
  const navigate = useNavigate();

  // The order was passed to us from CustomerDashboard when it navigated here
  const order = location.state as {
    orderId: number; status: string; totalAmount: number;
    orderItems: {orderItemId: number; product: {name: string}; quantity: number; unitPrice: number; subtotal: number}[];
  } | null;

  // If someone lands on this page directly without an order, send them back
  if (!order) {
    navigate("/customer-dashboard");
    return null;
  }

  return (
    <div className="container" style={{ maxWidth: 600, margin: "60px auto" }}>
      <div className="panel" style={{ textAlign: "center" }}>
        <h1>Thank you for your order!</h1>
        <p style={{ color: "#6b7280", marginTop: 8 }}>
          Your order has been received and will be processed soon.
        </p>

        <div className="panel" style={{ marginTop: 24, textAlign: "left" }}>
          <p>
            <strong>Order ID:</strong> #{order.orderId}
          </p>
          <p>
            <strong>Status:</strong> {order.status}
          </p>

          <table style={{ marginTop: 16 }}>
            <thead>
              <tr>
                <th>Product</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              {order.orderItems.map(function (item) {
                return (
                  <tr key={item.orderItemId}>
                    <td>{item.product.name}</td>
                    <td>{item.quantity}</td>
                    <td>R{item.unitPrice}</td>
                    <td>R{item.subtotal}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <div className="cart-summary total" style={{ marginTop: 16 }}>
            <span>Total</span>
            <span>R{order.totalAmount}</span>
          </div>
        </div>

        <Link
          to="/customer-dashboard"
          className="btn btn-primary"
          style={{ marginTop: 24, display: "inline-block" }}
        >
          Back to Dashboard
        </Link>
      </div>
    </div>
  );
}

export default OrderConfirmation;
