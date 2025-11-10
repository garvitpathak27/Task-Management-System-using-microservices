FROM openjdk:17-jdk-slim-buster
LABEL authors="garvitpathak27"
WORKDIR /app
COPY build/libs/*.jar api-gateway.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","api-gateway.jar"]