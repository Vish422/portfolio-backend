# Portfolio Backend — Spring Boot

## Stack
Java 21, Spring Boot 3.5, Spring Security + JWT, PostgreSQL, JPA, Maven.

## 1. Create PostgreSQL database
```sql
CREATE DATABASE portfolio_db;
```

## 2. Configure database
Default values are in `application.properties`:
- DB URL: `jdbc:postgresql://localhost:5432/portfolio_db`
- username: `postgres`
- password: `postgres`

Prefer environment variables in real deployment:
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `UPLOAD_DIR`.

## 3. Run
```bash
mvn spring-boot:run
```
Or open the project in IntelliJ IDEA and run `PortfolioApplication`.

## Default owner
- username: `owner`
- password: `ChangeMe123!`

**Change this password before production.**

## Main APIs
Public:
- `GET /api/images`
- `GET /api/videos`
- `GET /api/categories`

Auth:
- `POST /api/auth/login`

Owner APIs require:
`Authorization: Bearer <JWT>`

Images:
- `GET /api/images/admin/all`
- `POST /api/images/admin` multipart: `title`, optional `description`, `categoryId`, `displayOrder`, `visible`, and `file`
- `PUT /api/images/admin/{id}` multipart
- `DELETE /api/images/admin/{id}`

Videos:
- `GET /api/videos/admin/all`
- `POST /api/videos/admin` multipart: `title`, optional `description`, `categoryId`, `durationSeconds`, `displayOrder`, `visible`, `file`, `thumbnail`
- `PUT /api/videos/admin/{id}` multipart
- `DELETE /api/videos/admin/{id}`

Categories:
- GET/POST/PUT/DELETE `/api/categories`

Uploaded files are stored under `uploads/images` and `uploads/videos` and served at `/uploads/...`.

## Frontend flow
1. Login → receive JWT.
2. Store token securely in the frontend.
3. Send `Authorization: Bearer TOKEN` for owner APIs.
4. Upload images/videos with `multipart/form-data`.
5. Public portfolio calls `/api/images` and `/api/videos` and renders the returned URLs.

## Production notes
For a real public portfolio, move large video/image files to object storage/CDN rather than keeping them on the application server. Also replace the seed owner password, use HTTPS, and restrict CORS to your actual frontend domain.
