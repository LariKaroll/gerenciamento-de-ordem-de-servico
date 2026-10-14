# maven
FROM maven:4.0.0-rc-4-amazoncorretto-17 AS build

COPY . .

WORKDIR /app
RUN mvn clean install

# java
FROM openjdk:17-ea-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]