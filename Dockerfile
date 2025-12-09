# Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /workspace/app
COPY pom.xml .
COPY src src/
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre

# Set working directory inside container
WORKDIR /app

# Copy the built JAR file from build stage
COPY --from=build /workspace/app/target/car-rental-api-0.0.1-SNAPSHOT.jar app.jar

# Create directory for file uploads
RUN mkdir -p /app/uploads

# Expose Spring Boot port
EXPOSE 8081

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
