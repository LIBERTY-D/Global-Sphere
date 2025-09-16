FROM maven:3.9.10-eclipse-temurin-21 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=builder /build/target/global-sphere-0.0.1-SNAPSHOT.jar app.jar

ENV APP_PORT=8080
EXPOSE ${APP_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
