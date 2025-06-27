# Step 1: Use Maven image with OpenJDK 17 to build the app
FROM maven:3.8.1-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Run Maven to compile the project and create the JAR file
RUN mvn clean package -DskipTests

# Step 2: Use OpenJDK 17 JRE image to run the app
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build image
COPY --from=build /app/target/rest-api-0.0.1-SNAPSHOT.jar /app/rest-api.jar

# Expose the default port (optional, but good practice for web apps)
EXPOSE 8080

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "rest-api.jar"]
