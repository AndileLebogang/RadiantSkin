import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import products from '../data/products';

const categoryInfo: Record<string, { title: string; tagline: string }> = {
  'skin-care': {
    title: 'Skin Care',
    tagline: 'Serums, moisturizers, and cleansers for a healthy, radiant complexion.',
  },
  'body-care': {
    title: 'Body Care',
    tagline: 'Nourishing lotions, scrubs, and oils for skin that feels as good as it looks.',
  },
  'hair-care': {
    title: 'Hair Care',
    tagline: 'Shampoos, masks, and oils formulated to strengthen and restore shine.',
  },
};

type ProductListingProps = {
  category: 'skin-care' | 'body-care' | 'hair-care';
};

function ProductListing({ category }: ProductListingProps) {
  const info = categoryInfo[category];
  const items = products[category] || [];

  return (
    <>
      <Navbar />

      <section className="shop-header">
        <div className="container">
          <span className="eyebrow">Shop by Category</span>
          <h1>{info.title}</h1>
          <p>{info.tagline}</p>
          <div className="category-tabs">
            <Link to="/shop/skin-care" className={category === 'skin-care' ? 'active' : ''}>Skin Care</Link>
            <Link to="/shop/body-care" className={category === 'body-care' ? 'active' : ''}>Body Care</Link>
            <Link to="/shop/hair-care" className={category === 'hair-care' ? 'active' : ''}>Hair Care</Link>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <div className="product-grid">
            {items.map((product) => (
              <div className="product-card card" key={product.id}>
                <img src={product.img} alt={product.name} />
                <div className="product-info">
                  <h4>{product.name}</h4>
                  <div className="product-price">R{product.price}</div>
                  <div className="product-actions">
                    <Link to={`/product/${product.id}`} className="btn btn-outline">View Details</Link>
                    <button className="btn btn-primary">Add to Cart</button>
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