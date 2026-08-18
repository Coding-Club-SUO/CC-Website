# ---------- Stage 1: Base/deps ----------
FROM node:20-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm ci

# ---------- Dev stage ----------
FROM base AS dev
COPY . .
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]

# ---------- Build stage ----------
FROM base AS builder
COPY . .
RUN npm run build

# ---------- Production stage ----------
FROM nginx:alpine AS runner
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]