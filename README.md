# RadiantSkin

RadiantSkin is a skincare storefront with a React frontend and a Spring Boot API. The frontend reads products and order summaries from the API; the API stores them in MySQL. A new database has no products, so the shop starts with an empty catalogue.

| Part | Technology | Local address |
| --- | --- | --- |
| Storefront | React 19, TypeScript, Vite 8 | `http://localhost:5173` |
| API | Java 21, Spring Boot 3.5 | `http://localhost:8080` |
| Database | MySQL 8 | `127.0.0.1:3306` by default |

## Local setup

You need Java 21, Node.js 24 with npm, and MySQL 8. This repository has no Docker Compose file or database seed script. Use a **new local database** for development: the API's current Hibernate setting is `ddl-auto=update`, which can change tables when the API starts. Never point the commands below at a database you need to preserve.

1. Create a database and a dedicated user in MySQL. For example, sign in to your local MySQL administrator account with `mysql -u root -p`, then run:

   ```sql
   CREATE DATABASE radiant_skin_dev;
   CREATE USER 'radiant_dev'@'127.0.0.1' IDENTIFIED BY '<choose a local password>';
   GRANT ALL PRIVILEGES ON radiant_skin_dev.* TO 'radiant_dev'@'127.0.0.1';
   ```

   Replace the password placeholder with a new local password. Keep it out of Git.

2. In a terminal at the repository root, point Spring Boot at that database and start the API:

   ```bash
   export SPRING_DATASOURCE_URL='jdbc:mysql://127.0.0.1:3306/radiant_skin_dev'
   export SPRING_DATASOURCE_USERNAME='radiant_dev'
   read -rsp 'Local database password: ' SPRING_DATASOURCE_PASSWORD; echo
   export SPRING_DATASOURCE_PASSWORD
   bash ./mvnw spring-boot:run
   ```

   In another terminal, `curl http://localhost:8080/product/getAll` checks the catalogue endpoint. `[]` is expected for a fresh database. The environment variables override the values in `src/main/resources/application.properties` for this process.

3. Start the storefront in a second terminal:

   ```bash
   cd frontend
   npm ci
   npm run dev
   ```

   Open `http://localhost:5173`. Vite forwards `/api/*` to the API on port 8080 and removes the `/api` prefix. To use another API address, set `API_PROXY_TARGET` in `frontend/.env.local` as shown in [`frontend/.env.example`](frontend/.env.example). On Windows, set the same Spring environment variables in PowerShell, then use `mvnw.cmd` and `npm.cmd` for the Maven and npm commands.

## Checks

Run frontend checks from `frontend/`:

```bash
npm test
npm run lint
npm run build
```

Backend tests write to their configured database. Create a **separate disposable MySQL database and user** for them. In the MySQL administrator session, run:

```sql
CREATE DATABASE radiant_skin_test;
CREATE USER 'radiant_test'@'127.0.0.1' IDENTIFIED BY '<choose a separate test password>';
GRANT ALL PRIVILEGES ON radiant_skin_test.* TO 'radiant_test'@'127.0.0.1';
```

Then, in a terminal at the repository root, run:

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://127.0.0.1:3306/radiant_skin_test'
export SPRING_DATASOURCE_USERNAME='radiant_test'
read -rsp 'Test database password: ' SPRING_DATASOURCE_PASSWORD; echo
export SPRING_DATASOURCE_PASSWORD
SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop bash ./mvnw test
```

These tests can insert, update, and delete records. Do not run them against your development or existing catalogue database.

## Current boundaries

The storefront supports browsing, filtering, a locally saved bag, order checkout and lookup, customer account creation at `/register`, and product reviews by registered customers. New customer passwords are hashed before storage. After registration, customers can log in at `/login`, view their account details at `/account`, and log out. Login uses a server session with an HttpOnly, SameSite=Lax cookie and a 30-minute idle timeout; the app never writes passwords or session IDs to localStorage or sessionStorage. Review submission uses the logged-in customer session and asks only for a rating and comment. Existing legacy review write routes and customer list/update API routes still lack access control, so account features are for local development until those routes are secured. Checkout saves an order, its items and delivery details through `POST /checkout/place`, calculates prices on the server, reduces stock in the same transaction, and clears the browser bag only after success. Retrying a request ID returns the existing order. The school assignment accepts arbitrary numeric card input locally; card values are never sent or saved, and no payment provider is called or money charged. Guests can still place orders. Signed-in checkout prefills the customer contact fields and links the order to the customer from the server session; an unverified email alone never links an account. A fresh database has no sample products, and this repository does not include a repeatable catalogue import. The frontend's [README](frontend/README.md) explains the existing API integration and its limitations.
