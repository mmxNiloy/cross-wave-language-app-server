# syntax=docker/dockerfile:1

# Use build args for flexibility
ARG JDK_VERSION=21
ARG APP_NAME=app

################################################################################
# Stage 1: Build the application
FROM gradle:9.1-jdk${JDK_VERSION} AS build

# Set working directory
WORKDIR /usr/src/app

# Copy Gradle wrapper and config first (for better layer caching)
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Download dependencies (cached separately)
RUN ./gradlew dependencies --no-daemon || return 0

# Copy the rest of the source code
COPY . .

# Build the Spring Boot fat JAR
RUN ./gradlew clean bootJar --no-daemon

################################################################################
# Stage 2: Run the application (runtime)
FROM eclipse-temurin:${JDK_VERSION}-jre-alpine AS final

# App folder
WORKDIR /usr/src/app

# Copy only the built JAR from the build stage
ARG APP_NAME
COPY --from=build /usr/src/app/build/libs/*.jar ${APP_NAME}.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
