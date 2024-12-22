FROM openjdk:17-jdk-slim
LABEL authors="Umang Rathod"

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY target/inventory-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8084

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]