# Use official OpenJDK 17 image
FROM maven:3.9.5-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
