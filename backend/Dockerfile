# Two images from one file:
#   docker build --target dev  -t cscu-backend:dev  ./backend
#   docker build --target prod -t cscu-backend:prod ./backend

# ---------- Shared: JDK + cached dependencies ----------
FROM eclipse-temurin:25-jdk-alpine AS base
WORKDIR /app
# Wrapper and build config first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null || true

# ================= DEV IMAGE =================
# Live reload. docker-compose.yml bind-mounts ./backend at /app. `classes -t`
# recompiles on every source change and Spring Boot DevTools restarts the app.
FROM base AS dev
ENV JAVA_ENV=dev
EXPOSE 8000
CMD ["sh", "-c", "sh ./gradlew -t classes -q & exec sh ./gradlew bootRun"]

# ---------- Build step for prod ----------
FROM base AS builder
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ================= PROD IMAGE =================
# Just the JRE and the built JAR: no Gradle, no source, no file watching
# (DevTools is excluded from bootJar). Runs as a non-root user. Request
# handling uses virtual threads (spring.threads.virtual.enabled), so it
# serves many concurrent users per instance.
FROM eclipse-temurin:25-jre-alpine AS prod
WORKDIR /app
ENV JAVA_ENV=prod \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"
RUN addgroup -S app && adduser -S app -G app
COPY --from=builder --chown=app:app /app/build/libs/*.jar app.jar
USER app
EXPOSE 8000
CMD ["java", "-jar", "app.jar"]
