# RadiantSkin frontend

Customer storefront built with React, TypeScript, and Vite. Product data comes exclusively from the Spring backend and its MySQL database.

## Run the storefront

Use Node 24 and run these commands from `frontend/` (PowerShell):

```powershell
npm.cmd ci
npm.cmd run dev
```

Open http://localhost:5173 with the backend running on port 8080. There is no sample catalogue, offline product cache, or demo mode. If the API is unavailable, the storefront shows an error instead of products. It rechecks on window focus, reconnection, and every 30 seconds; failed requests clear previously loaded products.

The visual design pairs Instrument Serif with Manrope, warm white, charcoal, terracotta accents, and editorial photography. The storefront, product pages, bag, checkout preview, and order lookup share the same responsive styles. Fonts and photographs are served locally; source and license details are in [font credits](public/fonts/CREDITS.md) and [image credits](public/images/CREDITS.md). Real product packshots are listed in [product image credits](public/images/products/CREDITS.md).

The homepage uses staggered headline entrances and a slow photograph reveal. Scroll reveals run once per element, including products loaded asynchronously. Sticky navigation, button feedback, product hover reveals, mobile menu entrances, and bag-count updates complete the motion system. All motion respects `prefers-reduced-motion`; content remains visible when observer APIs are unavailable, and keyboard focus exposes unrevealed content immediately. No scroll interception or continuous animation loop is used.

## Run against the existing backend

The storefront reads products through the backend API. A fresh database has no products; this repository does not currently include a catalogue import or seed script. See the [local setup guide](../README.md) for a safe database and API setup.

```powershell
npm.cmd run dev
```

The default Vite proxy forwards `/api/*` to `http://127.0.0.1:8080/*`, removing the `/api` prefix. To change the destination, copy `.env.example` to `.env.local`, set `API_PROXY_TARGET`, and restart Vite. Port 5173 is intentionally fixed.

| Frontend capability                                            | Existing backend endpoint                                        |
| -------------------------------------------------------------- | ---------------------------------------------------------------- |
| Catalogue, product details, search, category and stock filters | `GET /product/getAll`                                            |
| Order status lookup                                            | `GET /order/read/{orderId}`                                      |
| Product reviews                                                | `GET /review/product/{productId}`                                |
| Submit a review as a logged-in customer                        | `POST /review/submit` with session cookie, rating and comment     |
| Shopping bag                                                   | Local browser storage; product IDs and quantities only           |
| Checkout                                                       | `POST /checkout/place` saves an order and reduces stock atomically |

Product categories are identified from the existing subtype fields: `usageInstructions`, `hairConcern`, and `skinConcern`. Prices use South African rand. Search, sorting, and filters run on the fetched catalogue and remain in the URL. The API adapter validates prices, limits request duration, handles null order responses, and omits customer credentials from the frontend order model.

The bag enforces the available stock quantity, survives refreshes, and synchronizes between tabs. Removed or unavailable catalogue items remain visible for removal rather than silently disappearing. Contact and address drafts stay in page memory until checkout submits them to the backend; they are not stored in browser storage. Successful checkout clears the bag and shows the saved order number. Failed checkout preserves the bag.

Customers can create an account from the storefront's **Create account** link. The form validates required details, shows duplicate-email and connection errors, and confirms success without storing the password in browser storage. Existing customers can log in at `/login`, see their details at `/account`, and log out. The storefront restores its signed-in state using `GET /auth/session`, not browser-stored identity or credentials. `POST /auth/login` creates a server session and `POST /auth/logout` invalidates it; all requests include cookies. Sessions expire after 30 minutes of inactivity or a backend restart. Header, footer, registration confirmation and mobile navigation link to login.

Product pages load reviews from the review API. Logged-in customers can submit a rating and a 3–240 character review without entering their details again. The API derives ownership from the server session, ignores client-supplied identity, and rejects missing sessions and non-customer accounts. Guests can read reviews and log in with a return link to the product. An expired session refreshes the account state and prompts a new login. The review response contains only its rating, comment, date, and ID. The existing legacy review write routes and other customer/order routes still need access control before public deployment.

The three legacy `example.com` catalogue image URLs resolve to bundled RadiantSkin product illustrations in the API adapter, so existing databases need no data migration. The other nine current products use local retailer packshots. Missing, unsafe or failed image URLs use a local neutral image, with a text fallback if that asset also fails. Product cards, product details, the home collection and the bag share this image component.

## Current integration boundary

- The product API can return an empty catalogue from a fresh database. Product counts in an existing local database depend on its current contents.
- Storefront checkout sends product IDs, quantities, the displayed total, delivery details, and a request UUID. The server locks products, checks current prices and stock, and saves an order with cascading items in one transaction. The request UUID prevents duplicates on retry. The storefront uses its browser bag, independently of the legacy database cart routes.
- The backend has cookie-based login sessions but no payment-provider integration. Checkout supports guests, prefills signed-in customers’ contact details, stores a delivery/contact snapshot, and links signed-in orders using the server session. An unverified email does not identify an account. Delivery is complimentary. Order creation and lookup return a receipt DTO without customer credentials, contact details or entity cycles. Other legacy APIs still need access control before public deployment.
- For the school assignment, the card form accepts any nonempty numeric card number with spaces or hyphens, an MM/YY expiry and a 3–4 digit security code. It performs no bank validation or payment processing. Card fields never enter the checkout payload or browser storage and clear after every request. The website uses normal customer-facing checkout wording.

## Checks

```powershell
npm.cmd run test
npm.cmd run lint
npm.cmd run build
```

The frontend tests cover API shapes and failures, subtype mapping, invalid prices and IDs, saved-bag recovery, stock limits, filters, catalogue retry, review loading and posting, arbitrary card acceptance, successful checkout and persisted bag clearing, checkout failure and retry, card privacy, order lookup, navigation, backend failure and recovery, and empty database responses. They use a DOM test environment and mocked API responses. The frontend also tests login, incorrect credentials, connection errors, session restoration, logout, forged browser identity, mobile navigation and returning to checkout with prefilled details. The backend `AccountLoginTest` checks registration-to-login, session rotation and restoration, logout, invalid credentials, and authenticated checkout ownership; all test data rolls back. The backend `StorefrontCheckoutTest` checks database persistence, stock reduction, duplicate-request handling, receipt lookup and invalid input using transactions that roll back test data.

The UI includes responsive layouts, keyboard focus styles, labelled form controls, reduced-motion support, loading states, empty states, broken-image fallbacks, and retry actions. Checkout was also verified in the running local browser, including saved order confirmation and an empty bag.

## Production build

`npm.cmd run build` creates `dist/`. A static host must serve `index.html` for client routes such as `/shop` and `/products/1`. Configure a reverse proxy for `/api/*` that strips `/api`, or set `VITE_API_BASE_URL` to the backend origin before building and allow that frontend origin in the backend's existing CORS configuration.

`npm.cmd run preview` serves the production build locally on port 4173 with the API proxy. It requires the backend to load products, just like the development server.
