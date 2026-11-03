# syntax=docker/dockerfile:1

# -----------------------------------------------------------------------------
# Build
# -----------------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests

# -----------------------------------------------------------------------------
# Runtime
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

RUN apk upgrade --no-cache

COPY --from=build --chown=1000:1000 /app/target/backend-*.jar app.jar

USER 1000:1000

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
