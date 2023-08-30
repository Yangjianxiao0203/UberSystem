FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]