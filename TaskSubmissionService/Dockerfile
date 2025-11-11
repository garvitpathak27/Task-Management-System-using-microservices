FROM eclipse-temurin:17-jre-alpine

LABEL authors="garvitpathak27"

WORKDIR /app

# Install curl so container health checks work without extra layers
RUN apk add --no-cache curl

COPY build/libs/task-submission-service.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/app/app.jar"]
