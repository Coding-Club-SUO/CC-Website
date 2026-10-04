# CSCU Website

The website of the **Computer Science Course Union (CSCU)** at UBC Okanagan: a department-backed hub for Computer Science and Data Science students, run by students.

The site has two parts:

- **Club homepage** (`/`): who CSCU is, its history and direction, events, and how to get involved.
- **Resource Hub** (`/resourcehub`, `/resources`): a central place for COSC, MATH, PHYS, and STAT course resources such as past exams and student-made worksheets.

The project is built by CSCU members as a collaborative software engineering initiative, giving students experience with a real full-stack codebase while building tools for the university community.

CSCU is being restructured as a department-backed initiative in partnership with the UBC Okanagan Department of Computer Science. That work is still in progress.

---

## Features

- Club homepage with first-visit animations, About, Get Involved, Team and Events pages (calendar plus a seasonal archive timeline).
- Centralized repository for COSC, MATH, PHYS, and STAT course resources.
- Light and dark mode.
- Authentication for CSCU members (login page is still in development).

---

## Tech Stack

| Layer | Technology |
| --- | --- |
| Frontend | Next.js (App Router), React, TypeScript |
| Backend | Spring Boot, Java 25, Gradle |
| Database | PostgreSQL (Flyway migrations) |
| Cache | Redis (shared Next.js page cache) |
| File storage | SeaweedFS (S3 API) |
| Containers | Docker, Docker Compose |

```
Browser -> Frontend (Next.js) -> Backend API (Spring Boot) -> PostgreSQL / Redis / SeaweedFS
```

---

## Project Structure

```
CC-Website/
├── .github/workflows/        # CI: docker, gradle, node
├── backend/                  # Spring Boot API
│   ├── src/main/java/com/example/app
│   ├── src/main/resources/   # application.properties, application-prod.properties, db/migrations
│   ├── Dockerfile            # dev and prod targets
│   └── build.gradle
├── frontend/                 # Next.js app
│   ├── src/app/              # Routes (/, /resourcehub, /resources, /events, /about/*, /login)
│   ├── src/views/
│   │   ├── homepage/         # CSCU club homepage
│   │   ├── resourcehub/      # Resource Hub landing page
│   │   ├── resourcepage/     # Course resources list
│   │   ├── eventspage/       # Calendar and archive
│   │   └── aboutpage/        # About, Get Involved, Team
│   ├── src/content/          # Site copy, links, events (placeholder data)
│   ├── src/components/       # Navbar, Footer, theme toggle
│   └── Dockerfile            # dev and prod targets
├── infrastructure/           # PostgreSQL seed, SeaweedFS config
├── docker-compose.yml        # Development stack (live reload)
├── docker-compose.prod.yml   # Production overrides
└── .env.example
```

---

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Node.js 20+ and Java 25 (only if running outside Docker)

### Setup

```bash
git clone https://github.com/Coding-Club-SUO/CC-Website.git
cd CC-Website
cp .env.example .env     # then fill in the values
```

The `main` branch is protected. Push to a separate branch and open a pull request; it must be approved before merging.

---

## Development (Docker)

Development images watch for file changes: the source folders are mounted into the containers.

```bash
docker compose up --build
```

- **Frontend** (`cscu-frontend:dev`): `next dev` with hot reload, at http://localhost:3000 (`NEXT_PORT`).
- **Backend** (`cscu-backend:dev`): Gradle recompiles on save and Spring Boot DevTools restarts the app, at http://localhost:8000 (`SPRING_PORT`).
- Redis, PostgreSQL and SeaweedFS start alongside.

If you change dependencies, rebuild with `docker compose up --build -V` so the frontend `node_modules` volume is refreshed.

### Without Docker

```bash
cd backend && ./gradlew bootRun     # API (needs Postgres, Redis, SeaweedFS running)
cd frontend && npm install && npm run dev
cd backend && ./gradlew build       # build and run backend tests
cd frontend && npm run lint
```

The frontend depends on the backend; run both together.

---

## Production (Docker)

Production images do not watch files, contain no dev tooling, and run as a non-root user.

```bash
docker compose -f docker-compose.yml -f docker-compose.prod.yml up --build -d
```

- **Frontend** (`cscu-frontend:prod`): compiled Next.js standalone server with a healthcheck.
- **Backend** (`cscu-backend:prod`): JRE plus the built JAR, running with `JAVA_ENV=prod` (see `application-prod.properties`: SQL logging off, sample data seeding off, graceful shutdown). Requests use virtual threads, so one instance serves many concurrent users.
- **Multiple users / scaling:** run more copies, e.g. `--scale frontend=3`, behind a load balancer. Frontend replicas share the rendered-page cache through Redis. Remove the fixed host port mapping when scaling.
- **Redis is required** by the frontend; pages fail without it.

Build the images individually:

```bash
docker build --target dev  -t cscu-frontend:dev  ./frontend
docker build --target prod -t cscu-frontend:prod ./frontend
docker build --target dev  -t cscu-backend:dev   ./backend
docker build --target prod -t cscu-backend:prod  ./backend
```

Before a real deployment, replace every placeholder secret in `.env` and review `application.properties` (for example the dev `spring.security.user.password` and the SeaweedFS public endpoint).

---

## Useful Docker Commands

```bash
docker compose up -d                 # run in the background
docker compose down                  # stop everything
docker compose down -v               # stop and delete data volumes (deletes your data!)
docker compose logs -f backend       # follow backend logs
docker compose logs -f frontend      # follow frontend logs
docker compose restart backend       # restart one service
```

---

## License

See [LICENSE](LICENSE).
