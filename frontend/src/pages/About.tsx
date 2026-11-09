import { Link } from 'react-router-dom'
import { FiArrowRight } from 'react-icons/fi'
import './about.css'

export default function AboutPage() {
  return (
    <section className="section page about-page">
      <div className="page-heading">
        <span className="eyebrow">About RadiantSkin</span>
        <h1>Our story.</h1>
        <p>Skin, body and hair care that feels like a quiet moment, made just for you.</p>
      </div>

      <div className="about-story">
        <div className="about-story-title">
          <span className="eyebrow">Where it began</span>
          <h2>
            It started with a quiet <em>frustration.</em>
          </h2>
        </div>
        <div className="about-story-text">
          <p>
            Every shelf promised a miracle. Every label shouted louder than the last. Somewhere
            between the claims and the confusion, caring for your skin stopped feeling like care
            and started feeling like homework.
          </p>
          <p>
            RadiantSkin began with one simple question: what if it didn't have to be that way? What
            if your morning routine felt like a small moment that belongs only to you? A minute of
            softness before the day begins, and another at night to let it all go.
          </p>
          <p>
            So we built a shop around that feeling: a handpicked collection of essentials that are
            easy to understand, lovely to use and kind to your budget. Glowing isn't about being
            perfect. It's about feeling cared for.
          </p>
        </div>
      </div>

      <div className="about-pair">
        <article>
          <span className="eyebrow">Our mission</span>
          <h3>Care that feels like a treat.</h3>
          <p>
            To make everyday self-care simple, gentle and joyful, with clear information and fair
            prices, so choosing what's good for your skin never feels like a gamble.
          </p>
        </article>
        <article>
          <span className="eyebrow">Our vision</span>
          <h3>A little glow for everyone.</h3>
          <p>
            A South Africa where good skincare is easy to find, easy to trust and easy to love, one
            small, beautiful routine at a time.
          </p>
        </article>
      </div>

      <div className="about-values">
        <span className="eyebrow">What we care about</span>
        <h2>The little things.</h2>
        <div className="about-grid">
          <article>
            <span className="about-number">01</span>
            <h3>Gentle by design</h3>
            <p>Soothing essentials for every skin, body and hair concern, with tips on every product.</p>
          </article>
          <article>
            <span className="about-number">02</span>
            <h3>Honest and clear</h3>
            <p>Prices in rand, shown up front, with complimentary delivery. No surprises.</p>
          </article>
          <article>
            <span className="about-number">03</span>
            <h3>Safe checkout</h3>
            <p>Your card details are never stored, and every order can be tracked by its number.</p>
          </article>
        </div>
      </div>

      <div className="about-closing">
        <h2>
          Good care starts with <em>you.</em>
        </h2>
        <div className="about-links">
          <Link className="button" to="/shop">
            Start your glow <FiArrowRight />
          </Link>
          <Link className="text-link" to="/orders">
            Track your order <FiArrowRight />
          </Link>
        </div>
      </div>
    </section>
  )
}