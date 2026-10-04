# CSCU Frontend

The Next.js (App Router, TypeScript) frontend for the Computer Science Course Union website at UBC Okanagan.

## Routes

| Route | Page |
| --- | --- |
| `/` | CSCU club homepage (`src/views/homepage`) |
| `/resourcehub` | Resource Hub landing page (`src/views/resourcehub`) |
| `/resources` | Course resources list |
| `/events` | Events calendar and archive timeline |
| `/about/vision`, `/about/get-involved`, `/about/team` | About CSCU, Get Involved, Team |
| `/login` | Login (in development) |

Site copy, links and placeholder events live in `src/content/`. Global theme colours (dark and light) are CSS variables in `src/app/globals.css`.

## Scripts

```bash
npm install
npm run dev     # development server with hot reload
npm run build   # production build (standalone output)
npm run lint
```

The app needs Redis for its page cache (`cache-handler.js`) and the backend API (`API_BASE_URL`). Easiest is to run the whole stack from the repository root with `docker compose up --build`.

## Docker

`Dockerfile` has two targets:

```bash
docker build --target dev  -t cscu-frontend:dev  .   # next dev, file watching
docker build --target prod -t cscu-frontend:prod .   # compiled standalone server, non-root
```

See the root [README](../README.md) for the full dev and production setup.
