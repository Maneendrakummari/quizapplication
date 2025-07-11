# Use OpenJDK as base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the jar (ensure it's built as part of Git repo or CI/CD)
COPY target/quizapp-0.0.1-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
