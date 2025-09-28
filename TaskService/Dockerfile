LABEL authors="garvitpathak27"
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar task-service.jar
ENTRYPOINT ["java","-jar","/task-service.jar"]
EXPOSE 8082
