import React from 'react';
import { Link } from 'react-router-dom';

function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-grid">
          <div>
            <h4>RadiantSkin</h4>
            <p>Honest skincare for skin, body, and hair — formulated to work, priced to be worn every day.</p>
            <div className="social-links">
              <a href="#top">f</a><a href="#top">ig</a><a href="#top">x</a>
            </div>
          </div>
          <div>
            <h4>Contact</h4>
            <p>+27 12 345 6789</p>
            <p>hello@radiantskin.co.za</p>
            <p>123 Silver Lakes Rd, Pretoria</p>
          </div>
          <div>
            <h4>Shop</h4>
            <Link to="/shop/skin-care">Skin Care</Link>
            <Link to="/shop/body-care">Body Care</Link>
            <Link to="/shop/hair-care">Hair Care</Link>
          </div>
          <div>
            <h4>Company</h4>
            <Link to="/about">About Us</Link>
            <Link to="/login">Login</Link>
            <Link to="/register">Sign Up</Link>
          </div>
        </div>
        <div className="footer-bottom">© 2026 RadiantSkin. All rights reserved.</div>
      </div>
    </footer>
  );
}

export default Footer;