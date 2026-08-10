import React from 'react';
import { Link, useLocation } from 'react-router-dom';

function Navbar() {
  const location = useLocation();
  const isActive = (path) => (location.pathname === path ? 'active' : '');

  return (
    <nav className="navbar">
      <div className="logo">RadiantSkin</div>
      <ul className="nav-links">
        <li><Link to="/" className={isActive('/')}>Home</Link></li>
        <li><Link to="/shop" className={isActive('/shop')}>Shop</Link></li>
        <li><Link to="/about" className={isActive('/about')}>About Us</Link></li>
      </ul>
      <div className="nav-cta">
        <Link to="/login" className="btn btn-outline">Login</Link>
        <Link to="/register" className="btn btn-primary">Sign Up</Link>
      </div>
    </nav>
  );
}

export default Navbar;