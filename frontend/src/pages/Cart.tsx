import { useCallback, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

// This describes what one item in the cart looks like
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

const DELIVERY_FEE = 60;

function Cart() {
  const navigate = useNavigate();

  // This holds the list of items currently in the cart
  const [items, setItems] = useState<CartItemData[]>([]);

  // This function goes to the backend and gets the customer's cart
  const loadCart = useCallback(() => {
    // Get the logged in customer from local storage
    const customerText = localStorage.getItem("customer");

    // If nobody is logged in, send them to the login page
    if (!customerText) {
      navigate("/login");
      return;
    }

    const customer = JSON.parse(customerText);

    // Call the backend to get this customer's cart
    fetch("http://localhost:8080/cart/customer/" + customer.userId)
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        if (data && data.cartItems) {
          setItems(data.cartItems);
        } else {
          setItems([]);
        }
      });
  }, [navigate]);

  // Run loadCart() once, when the page first opens
  useEffect(function () {
    loadCart();
  }, [loadCart]);

  // Increase the quantity of one item by 1
  function increaseQuantity(item: CartItemData) {
    const newQuantity = item.quantity + 1;

    fetch("http://localhost:8080/cartitem/updateQuantity", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        cartItemId: item.cartItemId,
        quantity: newQuantity,
      }),
    }).then(function () {
      loadCart();
    });
  }

  // Decrease the quantity of one item by 1
  // If quantity is already 1, remove the item instead
  function decreaseQuantity(item: CartItemData) {
    if (item.quantity <= 1) {
      removeItem(item.cartItemId);
      return;
    }

    const newQuantity = item.quantity - 1;

    fetch("http://localhost:8080/cartitem/updateQuantity", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        cartItemId: item.cartItemId,
        quantity: newQuantity,
      }),
    }).then(function () {
      loadCart();
    });
  }

  // Remove one item from the cart completely
  function removeItem(cartItemId: number) {
    fetch("http://localhost:8080/cartitem/delete/" + cartItemId, {
      method: "DELETE",
    }).then(function () {
      loadCart();
    });
  }

  // Work out the totals to show at the bottom
  let subtotal = 0;
  for (let i = 0; i < items.length; i++) {
    subtotal = subtotal + items[i].product.price * items[i].quantity;
  }

  const delivery = items.length > 0 ? DELIVERY_FEE : 0;
  const total = subtotal + delivery;

  return (
    <>
      <Navbar />

      <div className="container">
        <div className="main-header">
          <div>
            <h1>Your Cart</h1>
            <p>Review items before checking out.</p>
          </div>
        </div>

        {items.length === 0 && (
          <div className="panel">
            <p>
              Your cart is empty.{" "}
              <Link to="/shop/skin-care">Browse products</Link> to add something
              you'll love.
            </p>
          </div>
        )}

        {items.length > 0 && (
          <>
            <div className="panel" style={{ marginBottom: 24 }}>
              {items.map(function (item) {
                return (
                  <div className="cart-item" key={item.cartItemId}>
                    <img src={item.product.imageUrl} alt={item.product.name} />

                    <div className="cart-item-info">
                      <h4>{item.product.name}</h4>
                      <div className="product-price">R{item.product.price}</div>
                    </div>

                    <div className="qty-control">
                      <button onClick={() => decreaseQuantity(item)}>-</button>
                      <span>{item.quantity}</span>
                      <button onClick={() => increaseQuantity(item)}>+</button>
                    </div>

                    <button
                      className="icon-btn"
                      onClick={() => removeItem(item.cartItemId)}
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
              <button
                className="btn btn-primary btn-block"
                style={{ marginTop: 18 }}
              >
                Checkout
              </button>
            </div>
          </>
        )}
      </div>

      <Footer />
    </>
  );
}

export default Cart;
