import "../App.css";
import { Link } from "react-router-dom";

const features = [
  {
    icon: "🌿",
    title: "Natural Ingredients",
    text: "Carefully selected botanical ingredients for healthy, glowing skin.",
  },
  {
    icon: "🐰",
    title: "Cruelty Free",
    text: "Our products are never tested on animals.",
  },
  {
    icon: "🚚",
    title: "Fast Delivery",
    text: "Quick and reliable delivery anywhere in South Africa.",
  },
  {
    icon: "🔒",
    title: "Secure Payments",
    text: "Shop safely using trusted payment methods.",
  },
];

const products = [
  {
    name: "Hydrating Cleanser",
    price: "R249",
    image: "https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=600",
  },
  {
    name: "Vitamin C Serum",
    price: "R349",
    image: "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600",
  },
  {
    name: "Daily Moisturiser",
    price: "R299",
    image: "https://images.unsplash.com/photo-1612817288484-6f916006741a?w=600",
  },
];
const categories = [
  {
    name: "Skin Care",
    image: "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=900",
    description: "Cleanse, hydrate and nourish your skin.",
  },
  {
    name: "Body Care",
    image: "https://images.unsplash.com/photo-1601612628452-9e99ced43524?w=900",
    description: "Pamper your body with luxurious daily care.",
  },
  {
    name: "Glow Care",
    image: "https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=900",
    description: "Reveal your natural glow with brightening products.",
  },
];

function Homepage() {
  return (
    <div className="homepage">
      {/* ==========================
          Navigation
      =========================== */}

      <nav className="navbar">
        <div className="logo">
          <h2>
            Radiant<span>Skin</span>
          </h2>
        </div>

        <ul className="nav-links">
          <li>
            <a href="#">Home</a>
          </li>

          <li>
            <a href="#">Shop</a>
          </li>

          <li>
            <a href="#">Collections</a>
          </li>

          <li>
            <a href="#">About</a>
          </li>

          <li>
            <a href="#">Contact</a>
          </li>
        </ul>

        <div className="nav-actions">
          <Link to="/login">
            <button className="login-button">Login</button>
          </Link>

          <Link to="/register">
            <button className="signup-button">Sign Up</button>
          </Link>
        </div>
      </nav>

      {/* ==========================
          Hero Section
      =========================== */}

      <section className="hero">
        <div className="hero-left">
          <h1>
            Beauty that feels
            <br />
            <span>calm, elevated</span>
            <br />
            and beautifully personal.
          </h1>

          <p>
            Discover luxurious skincare created with carefully selected
            ingredients that nourish, protect and restore your natural glow
            every day.
          </p>

          <div className="hero-buttons">
            <button className="shop-now">Shop Now</button>

            <button className="view-products">View Products →</button>
          </div>
        </div>

        <div className="hero-right">
          <div className="hero-circle"></div>

          <img
            src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=900"
            alt="Skincare Product"
            className="hero-image"
          />

          <div className="floating-card card-one">
            <h4>Glow Serum</h4>

            <p>Vitamin C</p>
          </div>

          <div className="floating-card card-two">
            <h4>Daily Ritual</h4>

            <p>Hydration + Care</p>
          </div>
        </div>
      </section>
      {/* ==========================
    Shop by Category
========================== */}

      <section className="categories-section">
        <div className="section-title">
          <span>SHOP BY CATEGORY</span>

          <h2>Find Your Perfect Skincare Routine</h2>

          <p>
            Whether you're starting your skincare journey or enhancing your
            routine, explore our carefully curated collections.
          </p>
        </div>

        <div className="categories-grid">
          {categories.map((category, index) => (
            <div className="category-card" key={index}>
              <img src={category.image} alt={category.name} />

              <div className="category-content">
                <h3>{category.name}</h3>

                <p>{category.description}</p>

                <button>Shop Now</button>
              </div>
            </div>
          ))}
        </div>
      </section>
      {/* ==========================
          Why Choose RadiantSkin
      =========================== */}

      <section className="features-section">
        <div className="section-title">
          <span>WHY CHOOSE US</span>

          <h2>Skincare Designed Around You</h2>

          <p>
            We combine carefully selected ingredients with modern skincare
            science to help you achieve naturally healthy skin.
          </p>
        </div>

        <div className="features-grid">
          {features.map((feature, index) => (
            <div className="feature-card" key={index}>
              <div className="feature-icon">{feature.icon}</div>

              <h3>{feature.title}</h3>

              <p>{feature.text}</p>
            </div>
          ))}
        </div>
      </section>

      {/* ==========================
          Featured Products
      =========================== */}

      <section className="products-section">
        <div className="section-title">
          <span>OUR COLLECTION</span>

          <h2>Featured Products</h2>
        </div>

        <div className="products-grid">
          {products.map((product, index) => (
            <div className="product-card" key={index}>
              <img src={product.image} alt={product.name} />

              <div className="product-content">
                <h3>{product.name}</h3>

                <p className="price">{product.price}</p>

                <button>View Product</button>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ==========================
          About Section
      =========================== */}

      <section className="about-section">
        <div className="about-image">
          <img
            src="https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=900"
            alt="RadiantSkin"
          />
        </div>

        <div className="about-content">
          <span>ABOUT RADIANTSKIN</span>

          <h2>Healthy skin begins with healthy choices.</h2>

          <p>
            At RadiantSkin, we believe skincare should feel effortless. Every
            product is carefully created using premium ingredients that nourish,
            protect and restore your skin while helping you build a simple daily
            routine.
          </p>

          <button>Learn More</button>
        </div>
      </section>

      {/* ==========================
          Footer
      =========================== */}

      <footer className="footer">
        <div className="footer-brand">
          <h2>
            Radiant<span>Skin</span>
          </h2>

          <p>
            Premium skincare inspired by nature, designed for everyday
            confidence.
          </p>
        </div>

        <div className="footer-links">
          <div>
            <h4>Shop</h4>

            <a href="#">Cleanser</a>

            <a href="#">Serums</a>

            <a href="#">Moisturisers</a>
          </div>

          <div>
            <h4>Company</h4>

            <a href="#">About</a>

            <a href="#">Contact</a>

            <a href="#">FAQ</a>
          </div>

          <div>
            <h4>Account</h4>

            <a href="#">Login</a>

            <a href="#">Sign Up</a>

            <a href="#">Cart</a>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Homepage;
