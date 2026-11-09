import { Link } from 'react-router-dom'
import { FiArrowRight } from 'react-icons/fi'
import './contact.css'

export default function ContactPage() {
  return (
    <section className="section page contact-page">
      <div className="page-heading">
        <span className="eyebrow">We'd love to hear from you</span>
        <h1>Contact us.</h1>
        <p>Questions about an order or a product? Reach us any of these ways.</p>
      </div>

      <div className="contact-pair">
        <article>
          <span className="eyebrow">Call us</span>
          <h3>021 555 0123</h3>
          <p>Landline, Monday to Friday, 08:00 to 17:00.</p>
        </article>
        <article>
          <span className="eyebrow">Email us</span>
          <h3>hello@radiantskin.co.za</h3>
          <p>We reply within one to two working days.</p>
        </article>
        <article>
  <span className="eyebrow">Find us</span>
  <h3>Cape Town, South Africa</h3>
  <p>Delivering skin, body and hair care across the country.</p>
</article>
       
      </div>

      <div className="contact-closing">
        <h2>
          Good care starts with <em>you.</em>
        </h2>
        <div className="contact-links">
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