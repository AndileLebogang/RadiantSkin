import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

function Shop() {
  return (
    <>
      <Navbar />

      <section className="shop-landing-hero">
        <div className="container">
          <span className="eyebrow">Shop RadiantSkin</span>
          <h1>Choose a category</h1>
          <p>Pick a range to start browsing our full collection.</p>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <div className="category-grid">
            <Link to="/shop/skin-care" className="category-card">
              <img src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500&q=80" alt="Skin Care" />
              <div className="category-overlay"><h3>Skin Care</h3></div>
            </Link>
            <Link to="/shop/body-care" className="category-card">
              <img src="https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=500&q=80" alt="Body Care" />
              <div className="category-overlay"><h3>Body Care</h3></div>
            </Link>
            <Link to="/shop/hair-care" className="category-card">
              <img src="https://images.unsplash.com/photo-1526947425960-945c6e72858f?w=500&q=80" alt="Hair Care" />
              <div className="category-overlay"><h3>Hair Care</h3></div>
            </Link>
          </div>
        </div>
      </section>

      <Footer />
    </>
  );
}

export default Shop;