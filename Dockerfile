# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy pom.xml and install dependencies
COPY pom.xml .
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the project
RUN mvn package -DskipTests

# Expose port
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/java-postgres-auth-0.0.1-SNAPSHOT.jar"]
