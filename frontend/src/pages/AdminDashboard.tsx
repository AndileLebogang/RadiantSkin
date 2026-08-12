import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

type ViewId = 'dashboard' | 'products' | 'customers' | 'orders' | 'payments' | 'reviews';

const NAV_ITEMS: { id: ViewId; icon: string; label: string }[] = [
  { id: 'dashboard', icon: '', label: 'Dashboard' },
  { id: 'products', icon: '', label: 'Products' },
  { id: 'customers', icon: '', label: 'Customers' },
  { id: 'orders', icon: '', label: 'Orders' },
  { id: 'payments', icon: '', label: 'Payments' },
  { id: 'reviews', icon: '', label: 'Reviews' },
];

function AdminDashboard() {
  const [activeView, setActiveView] = useState<ViewId>('dashboard');
  const navigate = useNavigate();

  return (
    <div className="admin-page">
      <div className="admin-layout">

        {/* ---------- SIDEBAR ---------- */}
        <aside className="sidebar">
          <div className="logo">RadiantSkin</div>
          <div className="role-tag">Admin Panel</div>

          <nav>
            {NAV_ITEMS.map((item) => (
              <div
                key={item.id}
                className={`nav-item ${activeView === item.id ? 'active' : ''}`}
                onClick={() => setActiveView(item.id)}
              >
                <span className="icon">{item.icon}</span> {item.label}
              </div>
            ))}
          </nav>

          <div className="logout">
            <div className="nav-item" onClick={() => navigate('/login')}>
              <span className="icon">↩</span> Logout
            </div>
          </div>
        </aside>

        {/* ---------- MAIN CONTENT ---------- */}
        <main className="main">

          {activeView === 'dashboard' && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Dashboard Overview</h1>
                  <p>Welcome back, here's what's happening today.</p>
                </div>
                <div className="admin-avatar">A</div>
              </div>

              <div className="stat-grid">
                <div className="stat-card">
                  <div className="stat-icon">🛍</div>
                  <div className="stat-label">Total Products</div>
                  <div className="stat-value">184</div>
                </div>
                <div className="stat-card">
                  <div className="stat-icon">👥</div>
                  <div className="stat-label">Total Customers</div>
                  <div className="stat-value">2,340</div>
                </div>
                <div className="stat-card">
                  <div className="stat-icon">📦</div>
                  <div className="stat-label">Total Orders</div>
                  <div className="stat-value">1,027</div>
                </div>
                <div className="stat-card">
                  <div className="stat-icon">💰</div>
                  <div className="stat-label">Total Sales</div>
                  <div className="stat-value">R412,900</div>
                </div>
              </div>

              <div className="panel">
                <div className="panel-head"><h3>Recent Orders</h3></div>
                <table>
                  <thead>
                    <tr><th>Order ID</th><th>Customer</th><th>Date</th><th>Amount</th><th>Status</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>#RS-1042</td><td>Naledi Mokoena</td><td>03 Aug 2026</td><td>R689</td><td><span className="pill pill-success">Delivered</span></td></tr>
                    <tr><td>#RS-1041</td><td>Thabo Khumalo</td><td>03 Aug 2026</td><td>R289</td><td><span className="pill pill-primary">Processing</span></td></tr>
                    <tr><td>#RS-1040</td><td>Amahle Peters</td><td>02 Aug 2026</td><td>R1,120</td><td><span className="pill pill-success">Delivered</span></td></tr>
                    <tr><td>#RS-1039</td><td>Sipho Dlamini</td><td>02 Aug 2026</td><td>R199</td><td><span className="pill pill-danger">Cancelled</span></td></tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === 'products' && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Products</h1>
                  <p>Manage your Skin, Body, and Hair Care catalogue.</p>
                </div>
                <button className="btn btn-primary">+ Add Product</button>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr><th>Product</th><th>Category</th><th>Price</th><th>Stock</th><th>Actions</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>Whipped Shea Body Butter</td><td>Body Care</td><td>R289</td><td>142</td>
                      <td className="table-actions"><button className="icon-btn">✎</button><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Keratin Repair Shampoo</td><td>Hair Care</td><td>R219</td><td>76</td>
                      <td className="table-actions"><button className="icon-btn">✎</button><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Vitamin C Face Serum</td><td>Skin Care</td><td>R399</td><td>58</td>
                      <td className="table-actions"><button className="icon-btn">✎</button><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Argan Shine Hair Oil</td><td>Hair Care</td><td>R289</td><td>0</td>
                      <td className="table-actions"><button className="icon-btn">✎</button><button className="icon-btn">🗑</button></td></tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === 'customers' && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Customers</h1>
                  <p>View and manage registered customer accounts.</p>
                </div>
              </div>

              <div className="search-bar">
                <input type="text" placeholder="Search customers by name or email..." />
                <button className="btn btn-outline">Search</button>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr><th>Name</th><th>Email</th><th>Joined</th><th>Orders</th><th>Actions</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>Naledi Mokoena</td><td>naledi.m@email.com</td><td>14 Jan 2026</td><td>12</td>
                      <td className="table-actions"><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Thabo Khumalo</td><td>thabo.k@email.com</td><td>02 Feb 2026</td><td>4</td>
                      <td className="table-actions"><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Amahle Peters</td><td>amahle.p@email.com</td><td>19 Mar 2026</td><td>9</td>
                      <td className="table-actions"><button className="icon-btn">🗑</button></td></tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === 'orders' && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Orders</h1>
                  <p>Track and update the status of all customer orders.</p>
                </div>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr><th>Order ID</th><th>Customer</th><th>Items</th><th>Amount</th><th>Status</th><th>Actions</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>#RS-1042</td><td>Naledi Mokoena</td><td>3</td><td>R689</td><td><span className="pill pill-success">Delivered</span></td>
                      <td className="table-actions"><button className="icon-btn">👁</button></td></tr>
                    <tr><td>#RS-1041</td><td>Thabo Khumalo</td><td>1</td><td>R289</td><td><span className="pill pill-primary">Processing</span></td>
                      <td className="table-actions"><button className="icon-btn">👁</button></td></tr>
                    <tr><td>#RS-1040</td><td>Amahle Peters</td><td>4</td><td>R1,120</td><td><span className="pill pill-success">Delivered</span></td>
                      <td className="table-actions"><button className="icon-btn">👁</button></td></tr>
                    <tr><td>#RS-1039</td><td>Sipho Dlamini</td><td>1</td><td>R199</td><td><span className="pill pill-danger">Cancelled</span></td>
                      <td className="table-actions"><button className="icon-btn">👁</button></td></tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === 'payments' && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Payments</h1>
                  <p>Review payment transactions and their status.</p>
                </div>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr><th>Transaction ID</th><th>Order</th><th>Method</th><th>Amount</th><th>Status</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>TXN-88213</td><td>#RS-1042</td><td>Card</td><td>R689</td><td><span className="pill pill-success">Paid</span></td></tr>
                    <tr><td>TXN-88214</td><td>#RS-1041</td><td>EFT</td><td>R289</td><td><span className="pill pill-primary">Pending</span></td></tr>
                    <tr><td>TXN-88215</td><td>#RS-1040</td><td>Card</td><td>R1,120</td><td><span className="pill pill-success">Paid</span></td></tr>
                    <tr><td>TXN-88216</td><td>#RS-1039</td><td>Card</td><td>R199</td><td><span className="pill pill-danger">Refunded</span></td></tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

          {activeView === 'reviews' && (
            <section>
              <div className="main-header">
                <div>
                  <h1>Reviews</h1>
                  <p>Moderate customer reviews across all products.</p>
                </div>
              </div>

              <div className="panel">
                <table>
                  <thead>
                    <tr><th>Customer</th><th>Product</th><th>Rating</th><th>Comment</th><th>Actions</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>Naledi M.</td><td>Shea Body Butter</td><td>★★★★★</td><td>Absorbs so fast, love the smell.</td>
                      <td className="table-actions"><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Thabo K.</td><td>Keratin Shampoo</td><td>★★★★☆</td><td>Good but wish it lasted longer.</td>
                      <td className="table-actions"><button className="icon-btn">🗑</button></td></tr>
                    <tr><td>Guest0912</td><td>Vitamin C Serum</td><td>★☆☆☆☆</td><td>Spam / inappropriate content</td>
                      <td className="table-actions"><button className="icon-btn">🗑</button></td></tr>
                  </tbody>
                </table>
              </div>
            </section>
          )}

        </main>
      </div>
    </div>
  );
}

export default AdminDashboard;