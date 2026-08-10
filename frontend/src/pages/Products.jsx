import "./Products.css";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch("http://localhost:8080/product/getAll");

        if (!response.ok) {
          throw new Error("Failed to load products");
        }

        const data = await response.json();
        setProducts(data);
      } catch (err) {
        setError("Could not load products. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  return (
    <div className="products-page">
      <nav className="products-navbar">
        <Link to="/dashboard" className="logo">
          <h2>
            Radiant<span>Skin</span>
          </h2>
        </Link>

        <Link to="/dashboard" className="back-link">
          ← Back to Dashboard
        </Link>
      </nav>

      <div className="products-header">
        <h1>Our Products</h1>
        <p>Browse our full skincare collection.</p>
      </div>

      {loading && <p className="status-message">Loading products...</p>}

      {error && <p className="status-message error">{error}</p>}

      {!loading && !error && products.length === 0 && (
        <p className="status-message">No products available yet.</p>
      )}

      <div className="products-grid">
        {products.map((product) => (
          <div className="product-card" key={product.productId}>
            <img
              src={product.imageUrl || "https://via.placeholder.com/300"}
              alt={product.name}
            />

            <div className="product-info">
              <h3>{product.name}</h3>
              <p className="brand">{product.brand}</p>
              <p className="price">R{product.price}</p>

              {product.stockQuantity > 0 ? (
                <p className="in-stock">In Stock</p>
              ) : (
                <p className="out-of-stock">Out of Stock</p>
              )}

              <button disabled={product.stockQuantity === 0}>
                Add to Cart
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Products;
