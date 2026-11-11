# Lost & Found Campus Hub

A full-stack campus lost-and-found application. Users can register, sign in, post lost/found items with photos, search listings, and submit claims. An administrator can review posts.

## Stack
- React + Vite, JavaScript, CSS
- Java 17, Spring Boot 3.3, Spring Web, Spring Data JPA, Spring Security, JWT
- MySQL 8, Cloudinary

## Run locally
1. Install Java 17+, Maven, Node.js 20+, and MySQL 8.
2. Create a MySQL database named `lost_found` (or let Hibernate create it).
3. Copy `backend/.env.example` to `backend/.env` and set database, JWT, and Cloudinary values. Export these as environment variables before starting the backend; Spring does not load `.env` files automatically.
4. From `backend`, run `mvn spring-boot:run`.
5. Copy `frontend/.env.example` to `frontend/.env`, then from `frontend` run `npm install` and `npm run dev`.
6. Open the Vite URL, usually http://localhost:5173.

## First admin
Register an account normally, then promote it in MySQL:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

## Main API
- `POST /api/auth/register`, `POST /api/auth/login`
- `GET /api/items?type=LOST&category=...&q=...`
- `GET /api/items/{id}`, `POST /api/items` (multipart `data` JSON + optional `image`), `PUT /api/items/{id}`, `DELETE /api/items/{id}`
- `POST /api/items/{id}/claims`, `GET /api/claims/mine`
- `GET /api/admin/items/pending`, `PATCH /api/admin/items/{id}/review?approved=true`

JWT is sent as `Authorization: Bearer <token>`. Passwords are BCrypt hashed. Never put Cloudinary secrets or JWT secrets in frontend code. For a production deployment, configure HTTPS, stronger secrets, database migrations, upload limits, and moderation policies.
