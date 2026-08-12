import { useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const TABS = ['description', 'ingredients', 'benefits', 'reviews'] as const;
type Tab = typeof TABS[number];

function ProductDetails() {
  const [activeTab, setActiveTab] = useState<Tab>('description');
  const [qty, setQty] = useState(1);

  return (
    <>
      <Navbar />

      <div className="container">
        <div className="breadcrumb">
          <Link to="/">Home</Link> / <Link to="/shop/body-care">Body Care</Link> / Whipped Shea Body Butter
        </div>

        <div className="product-top">
          <img src="https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=700&q=80" alt="Whipped Shea Body Butter" />

          <div>
            <h1 className="product-name">Whipped Shea Body Butter</h1>
            <div className="product-rating">
              <span className="stars">★★★★★</span>
              <span>4.8 (126 reviews)</span>
            </div>
            <div className="product-price-lg">R289</div>
            <p className="product-desc">
              A rich, whipped body butter made with pure shea and cocoa butter. Melts into skin instantly,
              leaving it soft, hydrated, and lightly scented with vanilla and sandalwood for up to 24 hours of moisture.
            </p>

            <div className="qty-row">
              <div className="qty-control">
                <button onClick={() => setQty((q) => Math.max(1, q - 1))}>−</button>
                <span>{qty}</span>
                <button onClick={() => setQty((q) => q + 1)}>+</button>
              </div>
            </div>

            <div className="product-actions-lg">
              <button className="btn btn-primary">Add to Cart</button>
              <button className="btn btn-outline">Add to Wishlist</button>
            </div>
          </div>
        </div>

        <div className="tabs">
          {TABS.map((tab) => (
            <button
              key={tab}
              className={`tab-btn ${activeTab === tab ? 'active' : ''}`}
              onClick={() => setActiveTab(tab)}
            >
              {tab === 'reviews' ? 'Reviews (126)' : tab.charAt(0).toUpperCase() + tab.slice(1)}
            </button>
          ))}
        </div>

        {activeTab === 'description' && (
          <div className="tab-panel">
            <p>
              Our Whipped Shea Body Butter is crafted for deeply dry or sensitive skin. Unlike regular lotions,
              its whipped texture absorbs quickly without a greasy residue, delivering long-lasting hydration
              from the very first use. Best applied right after a shower while skin is still slightly damp.
            </p>
          </div>
        )}

        {activeTab === 'ingredients' && (
          <div className="tab-panel">
            <ul>
              <li>Organic Shea Butter (40%)</li>
              <li>Cocoa Butter</li>
              <li>Coconut Oil</li>
              <li>Vitamin E</li>
              <li>Sweet Almond Oil</li>
              <li>Natural Vanilla &amp; Sandalwood Extract</li>
            </ul>
          </div>
        )}

        {activeTab === 'benefits' && (
          <div className="tab-panel">
            <ul>
              <li>Deeply hydrates for up to 24 hours</li>
              <li>Soothes dry, cracked, or irritated skin</li>
              <li>Improves skin elasticity over time</li>
              <li>Free from parabens, sulfates, and silicones</li>
              <li>Suitable for sensitive skin</li>
            </ul>
          </div>
        )}

        {activeTab === 'reviews' && (
          <div className="tab-panel">
            <div className="review-item">
              <div className="review-head">
                <strong>Naledi M.</strong>
                <span className="stars">★★★★★</span>
              </div>
              <p>Absorbs so fast and smells amazing without being overpowering. My go-to now.</p>
            </div>
            <div className="review-item">
              <div className="review-head">
                <strong>Thabo K.</strong>
                <span className="stars">★★★★★</span>
              </div>
              <p>Bought this for my partner and she hasn't stopped using it since. Great texture.</p>
            </div>
            <div className="review-item">
              <div className="review-head">
                <strong>Amahle P.</strong>
                <span className="stars">★★★★☆</span>
              </div>
              <p>Really good for winter skin. Wish the jar was a bit bigger for the price.</p>
            </div>
          </div>
        )}
      </div>

      <Footer />
    </>
  );
}

export default ProductDetails;