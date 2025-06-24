# Use official Java image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/futurecollege-0.0.1-SNAPSHOT.jar"]
