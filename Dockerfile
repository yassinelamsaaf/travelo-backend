
# STAGE 1: Build the JAR (if needed)
FROM eclipse-temurin:23-jdk-alpine AS builder
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests
# STAGE 2: Runtime (Minimal & Secure)

FROM eclipse-temurin:23-jre-alpine

#to fix vulnerabilities
RUN apk update && apk upgrade --no-cache


RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]