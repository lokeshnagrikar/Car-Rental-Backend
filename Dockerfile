FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Install curl for healthcheck
RUN apk add  --no-cache curl

# Copy the JAR file
COPY target/car-rental-api-*.jar app.jar

# Create uploads directory
RUN mkdir -p /app/uploads/images

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]