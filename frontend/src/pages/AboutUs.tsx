import React from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

function AboutUs() {
  return (
    <>
      <Navbar />

      <section className="about-hero">
        <div className="container">
          <span className="eyebrow">About RadiantSkin</span>
          <h1>Skincare that respects your skin's story</h1>
          <p>We started RadiantSkin to make honest, effective skin, body, and hair care simple — no guesswork, no filler ingredients, just results you can feel.</p>
        </div>
      </section>

      <section className="section">
        <div className="container story-grid">
          <div>
            <span className="eyebrow">Our Story</span>
            <h2 className="section-title">Built from a simple frustration</h2>
            <p>RadiantSkin began when our founder couldn't find skincare that was both gentle and genuinely effective — most products promised results but delivered irritation instead.</p>
            <p>So we set out to build our own line: dermatologist-tested formulas, transparent ingredient lists, and packaging that doesn't cost the earth. Every product carries that same standard today.</p>
            <p>What started as a single serum has grown into a full range across skin, body, and hair — trusted by thousands who wanted skincare that simply works.</p>
          </div>
          <img src="https://images.unsplash.com/photo-1556228720-195a672e8a03?w=700&q=80" alt="RadiantSkin products arranged on a table" />
        </div>
      </section>

      <section className="section section-lavender">
        <div className="container">
          <div className="mv-grid">
            <div className="mv-card mission">
              <h3>Our Mission</h3>
              <p>To create effective, honest skincare that treats your skin with the same care we'd want for our own — free from harsh chemicals and empty promises.</p>
            </div>
            <div className="mv-card vision">
              <h3>Our Vision</h3>
              <p>A world where quality skincare is accessible to everyone, backed by real science and formulated with full transparency, one routine at a time.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <span className="eyebrow">Why RadiantSkin</span>
          <h2 className="section-title">Why customers choose us</h2>
          <p className="section-sub">A few things that set our products apart from the rest.</p>
          <div className="why-grid">
            <div className="why-card card">
              <div className="why-icon">🌿</div>
              <h4>Clean Ingredients</h4>
              <p>Every formula is free from parabens, sulfates, and unnecessary fillers.</p>
            </div>
            <div className="why-card card">
              <div className="why-icon">🔬</div>
              <h4>Dermatologist Tested</h4>
              <p>Each product is clinically tested for safety and effectiveness before launch.</p>
            </div>
            <div className="why-card card">
              <div className="why-icon">🌍</div>
              <h4>Sustainably Made</h4>
              <p>Recyclable packaging and responsibly sourced ingredients, always.</p>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </>
  );
}

export default AboutUs;