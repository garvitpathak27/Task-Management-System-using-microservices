LABEL authors="garvitpathak27"
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar user-service.jar
ENTRYPOINT ["java","-jar","/user-service.jar"]
EXPOSE 8084
