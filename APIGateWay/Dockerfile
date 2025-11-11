FROM eclipse-temurin:17-jre-alpine

LABEL authors="garvitpathak27"

WORKDIR /app

RUN apk add --no-cache curl

COPY build/libs/api-gateway.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java","-jar","/app/app.jar"]