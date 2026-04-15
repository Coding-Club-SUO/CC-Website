# ── Stage 1: Build ────────────────────────────
FROM eclipse-temurin:25-jdk-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and config first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Download dependencies before copying source
RUN ./gradlew dependencies --no-daemon || true

# Copy the rest of the source code
COPY src src

# Build the JAR, skipping tests
RUN ./gradlew bootJar --no-daemon -x test

# ── Stage 2: Run ──────────────────────────────
FROM eclipse-temurin:25-jre-alpine

# Set the working directory
WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the Spring Boot port
EXPOSE 8080

# Start the Spring Boot app
CMD ["java", "-jar", "app.jar"]