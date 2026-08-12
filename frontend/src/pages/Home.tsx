import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

function Home() {
  return (
    <>
      <Navbar />

      <section className="section">
        <div className="container hero">
          <div>
            <span className="eyebrow">Skin, Body &amp; Hair</span>
            <h1>Skincare made simple, honest, and effective.</h1>
            <p>
              Dermatologist-tested formulas across skin, body, and hair — free
              from harsh chemicals, priced for everyday use.
            </p>
            <div className="hero-actions">
              <Link to="/shop/body-care" className="btn btn-primary">
                Shop Now
              </Link>
              <Link to="/about" className="btn btn-outline">
                Learn More
              </Link>
            </div>
          </div>
          <img
            src="https://images.unsplash.com/photo-1556228720-195a672e8a03?w=700&q=80"
            alt="RadiantSkin hero"
          />
        </div>
      </section>

      <section className="section section-lavender">
        <div className="container">
          <span className="eyebrow">Shop by Category</span>
          <h2 className="section-title">Find your routine</h2>
          <p className="section-sub">Three ranges, one standard of quality.</p>
          <div className="category-grid">
            <Link to="/shop/skin-care" className="category-card">
              <img
                src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500&q=80"
                alt="Skin Care"
              />
              <div className="category-overlay">
                <h3>Skin Care</h3>
              </div>
            </Link>
            <Link to="/shop/body-care" className="category-card">
              <img
                src="https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=500&q=80"
                alt="Body Care"
              />
              <div className="category-overlay">
                <h3>Body Care</h3>
              </div>
            </Link>
            <Link to="/shop/hair-care" className="category-card">
              <img
                src="https://images.unsplash.com/photo-1526947425960-945c6e72858f?w=500&q=80"
                alt="Hair Care"
              />
              <div className="category-overlay">
                <h3>Hair Care</h3>
              </div>
            </Link>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <span className="eyebrow">Why Choose Us</span>
          <h2 className="section-title">What makes RadiantSkin different</h2>
          <div className="why-strip">
            <div className="why-card card">
              <div className="why-icon">🌿</div>
              <h4>Clean Ingredients</h4>
              <p>No parabens, sulfates, or fillers.</p>
            </div>
            <div className="why-card card">
              <div className="why-icon">🔬</div>
              <h4>Dermatologist Tested</h4>
              <p>Clinically tested for safety.</p>
            </div>
            <div className="why-card card">
              <div className="why-icon">🌍</div>
              <h4>Sustainably Made</h4>
              <p>Recyclable, responsibly sourced.</p>
            </div>
            <div className="why-card card">
              <div className="why-icon">🚚</div>
              <h4>Fast Delivery</h4>
              <p>Nationwide shipping across SA.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="section section-lavender">
        <div className="container">
          <span className="eyebrow">Featured Products</span>
          <h2 className="section-title">Customer favourites</h2>
          <p className="section-sub">Our best-selling products this month.</p>
          <div className="featured-grid">
            <div className="product-card card">
              <img
                src="https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=400&q=80"
                alt="Whipped Shea Body Butter"
              />
              <div className="product-info">
                <h4>Whipped Shea Body Butter</h4>
                <div className="product-price">R289</div>
                <div className="product-actions">
                  <Link to="/product/1" className="btn btn-outline">
                    View Details
                  </Link>
                  <button className="btn btn-primary">Add to Cart</button>
                </div>
              </div>
            </div>
            <div className="product-card card">
              <img
                src="https://images.unsplash.com/photo-1526947425960-945c6e72858f?w=400&q=80"
                alt="Keratin Repair Shampoo"
              />
              <div className="product-info">
                <h4>Keratin Repair Shampoo</h4>
                <div className="product-price">R219</div>
                <div className="product-actions">
                  <Link to="/product/9" className="btn btn-outline">
                    View Details
                  </Link>
                  <button className="btn btn-primary">Add to Cart</button>
                </div>
              </div>
            </div>
            <div className="product-card card">
              <img
                src="https://images.unsplash.com/photo-1611930022073-b7a4ba5fcccd?w=400&q=80"
                alt="Vitamin C Body Serum"
              />
              <div className="product-info">
                <h4>Vitamin C Body Serum</h4>
                <div className="product-price">R349</div>
                <div className="product-actions">
                  <Link to="/product/7" className="btn btn-outline">
                    View Details
                  </Link>
                  <button className="btn btn-primary">Add to Cart</button>
                </div>
              </div>
            </div>
            <div className="product-card card">
              <img
                src="https://images.unsplash.com/photo-1626015449059-fabbaea6c993?w=400&q=80"
                alt="Argan Shine Hair Oil"
              />
              <div className="product-info">
                <h4>Argan Shine Hair Oil</h4>
                <div className="product-price">R289</div>
                <div className="product-actions">
                  <Link to="/product/11" className="btn btn-outline">
                    View Details
                  </Link>
                  <button className="btn btn-primary">Add to Cart</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="container about-preview">
          <img
            src="https://images.unsplash.com/photo-1556228720-195a672e8a03?w=700&q=80"
            alt="About RadiantSkin"
          />
          <div>
            <span className="eyebrow">About RadiantSkin</span>
            <h2 className="section-title">Honest skincare, made simple</h2>
            <p>
              We started RadiantSkin to make effective, transparent skincare
              accessible — no guesswork, no filler ingredients, just results you
              can feel.
            </p>
            <Link to="/about" className="btn btn-primary">
              Read Our Story
            </Link>
          </div>
        </div>
      </section>

      <Footer />
    </>
  );
}

export default Home;
