# Use OpenJDK 17 base image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Maven config first
COPY pom.xml ./

# Copy source code
COPY src ./src

# Install Maven wrapper
RUN wget https://repo.maven.apache.org/maven-wrapper/maven-wrapper-3.8.8.zip \
    && unzip maven-wrapper-3.8.8.zip -d ./ \
    && rm maven-wrapper-3.8.8.zip

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the Spring Boot app
CMD ["java","-jar","target/java-postgres-auth-0.0.1-SNAPSHOT.jar"]
