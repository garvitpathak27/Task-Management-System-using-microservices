FROM eclipse-temurin:17-jre-alpine

LABEL authors="garvitpathak27"

WORKDIR /app

# Install curl to support Docker health checks
RUN apk add --no-cache curl

COPY build/libs/task-user-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]
