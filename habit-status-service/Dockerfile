# Stage 1: Build the app using Maven
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR using Java 23
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
