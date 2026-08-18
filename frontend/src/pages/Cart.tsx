import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import CartItem from "./CartItem";

export interface CartLineItem {
    productId: number;
    name: string;
    price: number;
    imageUrl: string;
    quantity: number;
}

const CART_STORAGE_KEY = "cart";
const DELIVERY_FEE = 60;

export function getCart(): CartLineItem[] {
    const stored = localStorage.getItem(CART_STORAGE_KEY);
    if (!stored) return [];
    try {
        return JSON.parse(stored) as CartLineItem[];
    } catch {
        return [];
    }
}

export function saveCart(items: CartLineItem[]) {
    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(items));
}

export function addToCart(
    product: {productId: number; name: string; price: number; imageUrl: string},
    quantity: number = 1
): CartLineItem[] {
    const items = getCart();
    const existing = items.find((item) => item.productId === product.productId);

    if (existing) {
        existing.quantity += quantity;
    } else {
        items.push({ ...product, quantity });
    }

    saveCart(items);
    return items;
}

function Cart() {
    const [items, setItems] = useState<CartLineItem[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        setItems(getCart());
    }, []);

    const updateItems = (next: CartLineItem[]) => {
        setItems(next);
        saveCart(next);
    };

    const handleIncrease = (productId: number) => {
        updateItems(
            items.map((item) =>
            item.productId === productId ? { ...item, quantity: item.quantity + 1 } : item)
        );
    };

    const handleDecrease = (productId: number) => {
        const target = items.find((item) => item.productId === productId);
        if (target && target.quantity <= 1) {
            handleRemove(productId);
            return;
        }
        updateItems(
            items.map((item) =>
            item.productId === productId ? { ...item, quantity: item.quantity - 1 } : item)
        );
    };

    const handleRemove = (productId: number) => {
        updateItems(items.filter((item) => item.productId !== productId));
    };

    const subtotal = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
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
 
        {items.length === 0 ? (
          <div className="panel">
            <p>
              Your cart is empty. <Link to="/shop">Browse products</Link> to add
              something you'll love.
            </p>
          </div>
        ) : (
          <>
            <div className="panel" style={{ marginBottom: 24 }}>
              {items.map((item) => (
                <CartItem
                  key={item.productId}
                  productId={item.productId}
                  name={item.name}
                  price={item.price}
                  imageUrl={item.imageUrl}
                  quantity={item.quantity}
                  onIncrease={handleIncrease}
                  onDecrease={handleDecrease}
                  onRemove={handleRemove}
                />
              ))}
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
                onClick={() => navigate("/checkout")}
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