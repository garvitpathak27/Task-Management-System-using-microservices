LABEL authors="garvitpathak27"
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar"]
EXPOSE 8090
