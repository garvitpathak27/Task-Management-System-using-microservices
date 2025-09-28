LABEL authors="garvitpathak27"
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar task-user-service.jar
ENTRYPOINT ["java","-jar","/task-user-service.jar"]
EXPOSE 8081
