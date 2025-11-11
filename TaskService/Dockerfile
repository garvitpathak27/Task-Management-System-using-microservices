FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="garvitpathak27"
LABEL description="Task Management Service - Microservice for managing tasks"
LABEL version="1.0"

WORKDIR /app

# Install curl for Docker health checks
RUN apk add --no-cache curl

COPY build/libs/task-service.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
