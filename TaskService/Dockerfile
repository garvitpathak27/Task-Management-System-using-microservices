FROM openjdk:17-jdk-slim

LABEL maintainer="garvitpathak27"
LABEL description="Task Management Service - Microservice for managing tasks"
LABEL version="1.0"

VOLUME /tmp

WORKDIR /app

COPY build/libs/*.jar task-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "task-service.jar"]
