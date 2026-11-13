import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

const categoryInfo: Record<
  string,
  { title: string; tagline: string; endpoint: string }
> = {
  "skin-care": {
    title: "Facial Care",
    tagline:
      "Serums, moisturizers, and cleansers for a healthy, radiant complexion.",
    endpoint: "skincare",
  },
  "body-care": {
    title: "Body Care",
    tagline:
      "Nourishing lotions, scrubs, and oils for skin that feels as good as it looks.",
    endpoint: "bodycare",
  },
  "hair-care": {
    title: "Hair Care",
    tagline:
      "Shampoos, masks, and oils formulated to strengthen and restore shine.",
    endpoint: "haircare",
  },
};

type ProductListingProps = {
  category: "skin-care" | "body-care" | "hair-care";
};

function ProductListing({ category }: ProductListingProps) {
  const navigate = useNavigate();
  const info = categoryInfo[category];

  const [products, setProducts] = useState<{productId: number; name: string; imageUrl: string; price: number}[]>([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetch(`http://localhost:8080/${info.endpoint}/getAll`)
      .then((response) => response.json())
      .then((data) => setProducts(data));
  }, [info.endpoint]);

  const handleAddToCart = (productId: number) => {
    const stored = localStorage.getItem("customer");

    if (!stored) {
      navigate("/register");
      return;
    }

    const customer = JSON.parse(stored);

    fetch("http://localhost:8080/cartitem/add", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        customerId: customer.userId,
        productId: productId,
        quantity: 1,
      }),
    }).then(() => {
      setMessage("Added to cart!");
      setTimeout(() => setMessage(""), 2000);
    });
  };

  return (
    <>
      <Navbar />

      <section className="shop-header">
        <div className="container">
          <span className="eyebrow">Shop by Category</span>
          <h1>{info.title}</h1>
          <p>{info.tagline}</p>
          <div className="category-tabs">
            <Link
              to="/shop/skin-care"
              className={category === "skin-care" ? "active" : ""}
            >
              Facial Care
            </Link>
            <Link
              to="/shop/body-care"
              className={category === "body-care" ? "active" : ""}
            >
              Body Care
            </Link>
            <Link
              to="/shop/hair-care"
              className={category === "hair-care" ? "active" : ""}
            >
              Hair Care
            </Link>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          {message && (
            <p className="auth-error" style={{ color: "green" }}>
              {message}
            </p>
          )}

          <div className="product-grid">
            {products.map((product) => (
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
                    <button
                      className="btn btn-primary"
                      onClick={() => handleAddToCart(product.productId)}
                    >
                      Add to Cart
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <Footer />
    </>
  );
}

export default ProductListing;
