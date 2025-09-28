LABEL authors="garvitpathak27"
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar task-submission-service.jar
ENTRYPOINT ["java","-jar","/task-submission-service.jar"]
EXPOSE 8083
