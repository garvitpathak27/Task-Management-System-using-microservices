FROM openjdk:17-jdk-slim-buster

LABEL authors="garvitpathak27"
LABEL version="1.0.0"
LABEL description="Eureka Server for Task Management Microservices"

# Create application user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Set working directory
WORKDIR /app

# Create directory for temporary files
VOLUME /tmp

# Copy the JAR file
COPY build/libs/eureka-server.jar app.jar

# Change ownership to spring user
RUN chown spring:spring /app/app.jar

# Switch to non-root user
USER spring:spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8085/actuator/health || exit 1

# Expose port
EXPOSE 8085

# Set JVM options for container environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=docker"]