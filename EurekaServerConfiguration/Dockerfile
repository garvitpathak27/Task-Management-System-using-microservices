FROM eclipse-temurin:17-jre-alpine

LABEL authors="garvitpathak27"
LABEL version="1.0.0"
LABEL description="Eureka Server for Task Management Microservices"

WORKDIR /app

RUN apk add --no-cache curl \
    && addgroup -S spring \
    && adduser -S spring -G spring

COPY build/libs/eureka-server.jar app.jar

RUN chown spring:spring /app/app.jar

USER spring:spring

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -sf http://localhost:8085/actuator/health || exit 1

EXPOSE 8085

ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=docker"]