# Web-App Builder (Meta)

A web app to let people make better web apps (meta, right?).
This is a full-stack playground for building and testing web-application features quickly.

* **Backend:** Spring Boot (Java 17+) + PostgreSQL 17
* **Frontend:** React (Vite)

It’s a playground for learning, and it’s also where I’m experimenting with cool stuff like:

* 📱 **Mobile design** — dev tools that actually work on your phone
* 🧱 **Web app building tools** — from idea to deployment, smoother
* 🕹️ **WebAssembly** — yes, I do want to run Doom in the browser (fingers crossed 🤞)
* 🧠 **AI-assisted features** — code hints, smarter UI suggestions, generative content
* 🐛 **Breaking things** just to see if I can fix them (a crucial step in the dev process)

> These are planned features that will (hopefully) land soon.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Quick Start (Docker Compose)](#quick-start-docker-compose)
3. [Run Locally (no Docker)](#run-locally-no-docker)
4. [Configuration](#configuration)
5. [Common Commands](#common-commands)
6. [Troubleshooting](#troubleshooting)
7. [Conventions](#conventions)
8. [License](#license)

---

## Project Structure

```
.

├── backend/
│   └── spring-api/          # Spring Boot app (Java 17+)
├── frontend/
│   └── react/               # Vite + React app
└── docker-compose.yml       # Dev stack
```

---

## Quick Start (Docker Compose)

**Requirements**

* Docker Desktop or Docker Engine (Compose v2+)
* Ports available: `5432`, `8088`, `5173`

Create a `docker-compose.yml` at the repo root with the **fixed** setup below:

```yaml
version: "3.9"

services:
  db:
    image: postgres:17
    container_name: postgres
    environment:
      POSTGRES_USER: hossein
      POSTGRES_PASSWORD: password
      POSTGRES_DB: customer
      PGDATA: /var/lib/postgresql/data
    volumes:
      - db-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - app-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U hossein -d customer"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: unless-stopped

  spring-api:
    container_name: spring-api
    image: blackpuss/spring-project:latest
    # If you have a Dockerfile for the backend, you can build it instead:
    # build:
    #   context: ./backend/spring-api
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/customer
      SPRING_DATASOURCE_USERNAME: hossein
      SPRING_DATASOURCE_PASSWORD: password
      # Optional:
      # SPRING_JPA_HIBERNATE_DDL_AUTO: update
      # SPRING_PROFILES_ACTIVE: docker
    depends_on:
      db:
        condition: service_healthy
    ports:
      - "8088:8080"
    networks:
      - app-net
    restart: unless-stopped

  frontend:
    container_name: frontend-react
    image: blackpuss/react:latest
    build:
      context: ./frontend/react
    working_dir: /app
    environment:
      NODE_ENV: development
      VITE_API_BASE_URL: http://spring-api:8080
      CHOKIDAR_USEPOLLING: "true"     # better file watching in containers
      WATCHPACK_POLLING: "true"
    command: sh -c "npm ci && npm run dev -- --host 0.0.0.0 --port 5173"
    volumes:
      - ./frontend/react:/app
      - /app/node_modules
    ports:
      - "5173:5173"
    depends_on:
      - spring-api
    networks:
      - app-net
    restart: unless-stopped

networks:
  app-net:
    driver: bridge

volumes:
  db-data:
```

### Bring it up

```bash
docker compose up -d --build
```

* Frontend (Vite): [http://localhost:5173](http://localhost:5173)
* Backend (Spring): [http://localhost:8088](http://localhost:8088)
* PostgreSQL: `localhost:5432` (user: `hossein`, password: `password`, db: `customer`)

### Tear it down

```bash
docker compose down -v
```

> ⚠️ `-v` deletes the named volume (database data). Omit it if you want to keep data.

---

## Run Locally (no Docker)

### 1) Database

Install PostgreSQL **17** locally (or run a throwaway container):

```bash
docker run --name pg-local -p 5432:5432 -e POSTGRES_USER=hossein -e POSTGRES_PASSWORD=password -e POSTGRES_DB=customer -d postgres:17
```

### 2) Backend (Spring Boot, Java 17+)

From `backend/spring-api`:

```bash
# macOS/Linux
./mvnw spring-boot:run

# Windows (PowerShell)
.\mvnw spring-boot:run
```

> You can also use a system Maven: `mvn spring-boot:run`.

Make sure the app can see your DB. Either configure `application.yml` or set env vars:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/customer
export SPRING_DATASOURCE_USERNAME=hossein
export SPRING_DATASOURCE_PASSWORD=password
```

The API will be available at: `http://localhost:8080` (or `8088` if you changed the port).

### 3) Frontend (Vite + React)

From `frontend/react`:

```bash
npm install
# Point the frontend at the backend. If backend runs on 8088 (via Docker), use that:
echo "VITE_API_BASE_URL=http://localhost:8088" > .env.local

# If backend runs locally on 8080 (no Docker), use this instead:
# echo "VITE_API_BASE_URL=http://localhost:8080" > .env.local

npm run dev
```

Open `http://localhost:5173`.

---

## Configuration

### Frontend (`frontend/react`)

* **API base URL** (required):

  Create `.env.local`:

  ```
  VITE_API_BASE_URL=http://localhost:8088
  ```

  When running all three services with Compose, the frontend talks to `http://spring-api:8080` **inside** the network. That’s set via `VITE_API_BASE_URL` in the Compose file.

### Backend (`backend/spring-api`)

* Uses standard Spring Boot env variables:

  * `SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/customer`
  * `SPRING_DATASOURCE_USERNAME=hossein`
  * `SPRING_DATASOURCE_PASSWORD=password`
* If you’re using JPA and want auto schema management in dev:

  * `SPRING_JPA_HIBERNATE_DDL_AUTO=update` (dev only)
* CORS (example, if needed):

  * Allow `http://localhost:5173` for local dev or configure in `application.yml`.

### Database

* **Default dev credentials** (do **not** use in production):

  * User: `hossein`
  * Password: `password`
  * DB: `customer`

---

## Common Commands

```bash
# Build everything fresh and start
docker compose up -d --build

# See logs
docker compose logs -f
docker compose logs -f spring-api
docker compose logs -f frontend
docker compose logs -f db

# Rebuild only frontend
docker compose up -d --build frontend

# Stop everything
docker compose down
```

---

## Troubleshooting

* **Frontend shows “Network Error” / CORS issues**

  * Ensure `VITE_API_BASE_URL` points to the right host/port.
  * If frontend runs on host and backend in Docker mapped to `8088`, use `http://localhost:8088`.
  * Configure CORS in Spring to allow `http://localhost:5173`.

* **Frontend in Docker can’t reach backend**

  * Inside Compose, use the **service name**: `http://spring-api:8080` (not `localhost`).
  * This is already set via `VITE_API_BASE_URL` in `docker-compose.yml`.

* **Postgres not ready when Spring starts**

  * Compose adds a healthcheck and `depends_on` with `service_healthy`. If you still see connection failures, `docker compose restart spring-api` after db is healthy.

* **Hot reload not working in container**

  * We enable polling (`CHOKIDAR_USEPOLLING`) and mount the source. Confirm your OS file sharing is enabled for the repo directory.

* **Port already in use**

  * Change the left side of the port mapping in `docker-compose.yml`, e.g., `5180:5173`, `8089:8080`, `5433:5432`.

* **Windows path issues**

  * Use WSL2 with Docker Desktop, and keep the project inside your Linux filesystem for best performance.

---

## Conventions

* **Java:** 17+
* **Node:** latest LTS recommended
* **Branch names:** `feature/*`, `fix/*`, `chore/*`
* **Commits:** Conventional Commits (e.g., `feat: add component builder`)
* **Env files:** never commit `.env*` to VCS

---

## License

MIT (or your preferred license). Add a `LICENSE` file to make it official.

---

### Notes on the fixes made to the original Compose snippet

* Pinned Postgres to `postgres:17` and added `POSTGRES_DB=customer` to match the JDBC URL.
* Swapped `PGDATA` to the standard `/var/lib/postgresql/data` and mounted a named volume `db-data`.
* Added a **healthcheck** for Postgres and made the Spring service wait for a healthy DB.
* Completed Spring datasource credentials via `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD`.
* Exposed Spring on host port **8088** (container 8080) and React on **5173** consistently.
* Ensured the frontend runs with `--host 0.0.0.0` so it’s reachable from outside the container.
* Set `VITE_API_BASE_URL` to talk to `spring-api` **inside** the Compose network; for local dev outside Docker, use `localhost`.
* Removed the empty `command:` from the original Spring service and provided a working frontend command.
* Kept `restart: unless-stopped` across services for dev convenience.
