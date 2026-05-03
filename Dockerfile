# Single-module Spring Boot app (sources under ./src)
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY gradlew gradlew.bat settings.gradle build.gradle gradle.properties ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S wallet && adduser -S wallet -G wallet
COPY --from=builder /app/build/libs/*.jar /app/app.jar
USER wallet
EXPOSE 8080
ENV SERVER_PORT=8080
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
