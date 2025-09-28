FROM openjdk:17-jdk-slim
LABEL authors="garvitpathak27"
VOLUME /tmp
COPY build/libs/*.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]
EXPOSE 8085