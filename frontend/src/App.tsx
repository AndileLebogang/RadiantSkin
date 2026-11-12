import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import {
  BrowserRouter,
  Link,
  NavLink,
  Route,
  Routes,
  useLocation,
  useNavigate,
} from 'react-router-dom'
import { FiArrowRight, FiArrowUpRight, FiMenu, FiSearch, FiShoppingBag, FiX } from 'react-icons/fi'
import { StoreProvider, useStore } from './StoreContext'
import { AuthProvider, useAuth } from './AuthContext'
import { useScrollReveal } from './useScrollReveal'
import RegisterPage from './pages/Register'
import LoginPage from './pages/Login'
import AccountPage from './pages/Account'
import MyReviewsPage from './pages/MyReviews'
import AboutPage from './pages/About'
import ContactPage from './pages/Contact'
import AdminPage from './pages/Admin'
import {
  HomePage,
  ShopPage,
  ProductPage,
  BagPage,
  CheckoutPage,
  OrdersPage,
  NotFoundPage,
} from './pages'

function Shell() {
  const { count, notice } = useStore()
  const { user, signOut } = useAuth()
  const [menu, setMenu] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const motionRoot = useScrollReveal(location.pathname)
  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'instant' })
  }, [location.pathname])
  async function logOut() {
    try {
      await signOut()
    } finally {
      setMenu(false)
      navigate('/')
    }
  }
  function search(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const value = new FormData(event.currentTarget).get('search')?.toString().trim() || ''
    navigate(`/shop${value ? `?q=${encodeURIComponent(value)}` : ''}`)
    setMenu(false)
  }
  return (
    <div className="storefront" ref={motionRoot}>
      <a className="skip-link" href="#main">
        Skip to content
      </a>
      <div className="announcement">
        <span>Considered essentials for your everyday.</span>
        <span>South Africa / ZAR</span>
      </div>
      <div className="navigation-shell">
        <header className="header">
          <Link to="/" className="wordmark" aria-label="RadiantSkin home">
            radiant<span>skin</span>
            <small>EVERYDAY CARE</small>
          </Link>
          <nav className="desktop-nav" aria-label="Main navigation">
            <NavLink to="/shop">Shop all</NavLink>
            <Link to="/shop?category=skin">Skin care</Link>
            <Link to="/shop?category=body">Body care</Link>
            <Link to="/shop?category=hair">Hair care</Link>
            <NavLink to="/about">About us</NavLink>
            <NavLink to="/contact">Contact</NavLink>
          </nav>
          <div className="header-actions">
            {user?.role === 'ADMIN' && (
              <Link className="account-link" to="/admin">
                Admin
              </Link>
            )}
            {user ? (
              <>
                {user.role === 'CUSTOMER' && (
                  <Link className="account-link" to="/my-reviews">
                    My reviews
                  </Link>
                )}
                <Link className="account-link" to="/account">
                  My account
                </Link>
              </>
            ) : (
              <>
                <Link className="account-link" to="/login">
                  Log in
                </Link>
                <Link className="account-link" to="/register">
                  Create account
                </Link>
              </>
            )}
            {user && (
              <button className="account-link logout-button" type="button" onClick={logOut}>
                Log out
              </button>
            )}
            <Link className="track-link" to="/orders">
              Track order
            </Link>
            <Link className="icon-button" to="/shop#search" aria-label="Search products">
              <FiSearch />
            </Link>
            <Link className="bag-link" to="/bag" aria-label={`Shopping bag, ${count} items`}>
              <FiShoppingBag />
              <span>Bag</span>
              <span className="bag-count" key={count}>
                ({count})
              </span>
            </Link>
            <button
              className="icon-button menu-toggle"
              aria-label={menu ? 'Close menu' : 'Open menu'}
              aria-expanded={menu}
              aria-controls="mobile-menu"
              onClick={() => setMenu(!menu)}
            >
              {menu ? <FiX /> : <FiMenu />}
            </button>
          </div>
        </header>
        {menu && (
          <div id="mobile-menu" className="mobile-menu">
            <nav aria-label="Mobile navigation" onClick={() => setMenu(false)}>
              <Link to="/shop">Shop all</Link>
              <Link to="/shop?category=skin">Skin care</Link>
              <Link to="/shop?category=body">Body care</Link>
              <Link to="/shop?category=hair">Hair care</Link>
              <Link to="/about">About us</Link>
              <Link to="/contact">Contact</Link>
              {user?.role === 'ADMIN' && <Link to="/admin">Admin</Link>}
              <Link to="/orders">Track order</Link>
              {user && (
                <button type="button" onClick={logOut}>
                  Log out
                </button>
              )}
              {user ? (
                <>
                  {user.role === 'CUSTOMER' && <Link to="/my-reviews">My reviews</Link>}
                  <Link to="/account">My account</Link>
                </>
              ) : (
                <>
                  <Link to="/login">Log in</Link>
                  <Link to="/register">Create account</Link>
                </>
              )}
            </nav>
            <form onSubmit={search}>
              <label className="sr-only" htmlFor="mobile-search">
                Search products
              </label>
              <input
                id="mobile-search"
                name="search"
                placeholder="Find your next essential"
                type="search"
              />
              <button className="icon-button" aria-label="Search">
                <FiArrowRight />
              </button>
            </form>
          </div>
        )}
      </div>
      <main id="main" tabIndex={-1}>
        <div className="route-content" key={location.pathname}>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/shop" element={<ShopPage />} />
            <Route path="/products/:id" element={<ProductPage />} />
            <Route path="/bag" element={<BagPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/orders" element={<OrdersPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/admin/register" element={<RegisterPage key="admin-register" admin />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/account" element={<AccountPage />} />
            <Route path="/my-reviews" element={<MyReviewsPage />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/contact" element={<ContactPage />} />
            <Route path="/admin" element={<AdminPage />} />
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </div>
      </main>
      <footer className="footer">
        <div className="footer-directory" data-reveal>
          <div className="footer-message">
            <span className="eyebrow">Your skin. Your standards.</span>
            <h2>
              Good care starts
              <br />
              with <em>you.</em>
            </h2>
            <Link className="text-link" to="/shop">
              Find your everyday <FiArrowUpRight />
            </Link>
          </div>
          <div className="footer-links">
            <span className="eyebrow">The collections</span>
            <Link to="/shop">Shop all</Link>
            <Link to="/shop?category=skin">Skin care</Link>
            <Link to="/shop?category=body">Body care</Link>
            <Link to="/shop?category=hair">Hair care</Link>
          </div>
          <div className="footer-links">
            <span className="eyebrow">At your service</span>
            <Link to="/about">About us</Link>
            <Link to="/contact">Contact us</Link>
            <Link to="/bag">Your shopping bag</Link>
            <Link to="/orders">Track your order</Link>
            {user ? (
              <>
                {user.role === 'CUSTOMER' && <Link to="/my-reviews">My reviews</Link>}
                <Link to="/account">Your account</Link>
              </>
            ) : (
              <>
                <Link to="/login">Log in to your account</Link>
                <Link to="/register">Create an account</Link>
              </>
            )}
            <p>
              South Africa
              <br />
              Prices in ZAR
            </p>
          </div>
        </div>
        <div className="footer-signature" aria-hidden="true">
          radiant skin
        </div>
        <div className="footer-bottom">
          <span>© {new Date().getFullYear()} RadiantSkin</span>
          <span>South Africa · ZAR</span>
          <span>Every day. On your terms.</span>
        </div>
      </footer>
      <div className={`toast ${notice ? 'visible' : ''}`} role="status" aria-live="polite">
        {notice}
      </div>
    </div>
  )
}
export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <StoreProvider>
          <Shell />
        </StoreProvider>
      </AuthProvider>
    </BrowserRouter>
  )
}
