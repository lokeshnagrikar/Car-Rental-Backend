# Use official lightweight Java 17 runtime as base image
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy the built JAR file from target/
COPY target/car-rental-api-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8081

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
