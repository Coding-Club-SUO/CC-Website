# ---------- Stage 1: Base/deps ----------
FROM node:20-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm ci

# ---------- Dev stage ----------
FROM base AS dev
COPY . .
EXPOSE 3000
CMD ["sh", "-c", "npm ci && npm run dev -- --hostname 0.0.0.0"]

# ---------- Build stage ----------
FROM base AS builder
COPY . .
RUN npm run build

# ---------- Production stage ----------
FROM node:20-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
COPY --from=builder /app/public ./public
EXPOSE 3000
CMD ["node", "server.js"]