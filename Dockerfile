# Build stage - compiles your Java code
FROM maven:3.9.0-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven configuration file
COPY pom.xml .
# Download all dependencies (caches them for faster builds)
RUN mvn dependency:go-offline -B

# Copy your source code
COPY src ./src

# Build your application into a JAR file
RUN mvn clean package -DskipTests

# Runtime stage - runs your application
FROM eclipse-temurin:21-jre
WORKDIR /app

# Create a non-root user for security
RUN addgroup --system --gid 1001 springgroup && \
    adduser --system --uid 1001 --gid 1001 springuser

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Switch to non-root user
USER springuser

# Expose port 8081 (your application port)
EXPOSE 8081

# Run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]