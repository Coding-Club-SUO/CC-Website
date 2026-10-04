# Two images from one file:
#   docker build --target dev  -t cscu-frontend:dev  ./frontend
#   docker build --target prod -t cscu-frontend:prod ./frontend

# ---------- Shared: dependencies ----------
FROM node:20-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm ci

# ================= DEV IMAGE =================
# Hot reload. Source is bind-mounted by docker-compose.yml, so file changes
# are picked up live. Polling is needed for bind mounts on Windows/macOS.
FROM base AS dev
ENV NODE_ENV=development \
    NEXT_TELEMETRY_DISABLED=1 \
    WATCHPACK_POLLING=true
COPY . .
EXPOSE 3000
CMD ["npm", "run", "dev", "--", "--hostname", "0.0.0.0"]

# ---------- Build step for prod ----------
FROM base AS builder
ENV NEXT_TELEMETRY_DISABLED=1
COPY . .
RUN npm run build

# ================= PROD IMAGE =================
# Compiled standalone server: no file watching, no dev dependencies, runs as a
# non-root user. Handles concurrent users; for more capacity run several
# replicas, which share rendered-page cache through Redis (cache-handler.js).
FROM node:20-alpine AS prod
WORKDIR /app
ENV NODE_ENV=production \
    NEXT_TELEMETRY_DISABLED=1 \
    PORT=3000 \
    HOSTNAME=0.0.0.0
RUN addgroup -S app && adduser -S app -G app
COPY --from=builder --chown=app:app /app/.next/standalone ./
COPY --from=builder --chown=app:app /app/.next/static ./.next/static
COPY --from=builder --chown=app:app /app/public ./public
USER app
EXPOSE 3000
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget -qO- http://127.0.0.1:3000/ >/dev/null || exit 1
CMD ["node", "server.js"]
