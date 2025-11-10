# Eureka Server Configuration

A Spring Boot microservice that acts as a service registry for the Task Management System. This Eureka Server provides service discovery capabilities for all microservices in the system.

## Features

- **Service Registration and Discovery**: Allows microservices to register themselves and discover other services
- **Health Monitoring**: Monitors the health of registered services
- **Load Balancing Support**: Provides client-side load balancing information
- **Circuit Breaker Integration**: Works with Hystrix for fault tolerance
- **Distributed Tracing**: Integrated with Zipkin for request tracing
- **Actuator Endpoints**: Comprehensive monitoring and management endpoints

## Prerequisites

- Java 17 or higher
- Gradle 8.5 or higher
- Docker (for containerized deployment)

## Configuration

### Local Development
The application uses `application.properties` for local development configuration:
- Server runs on port 8085
- Eureka dashboard available at http://localhost:8085

### Docker Deployment
The application uses `application-docker.properties` for Docker deployment:
- Configured for container networking
- Zipkin integration with container service names

## Building the Application

```bash
# Build with Gradle
./gradlew build

# Build Docker image
docker build -t eureka-server:1.0.0 .
```

## Running the Application

### Local Development
```bash
# Run with Gradle
./gradlew bootRun

# Run JAR file
java -jar build/libs/eureka-server.jar
```

### Docker
```bash
# Run Docker container
docker run -p 8085:8085 eureka-server:1.0.0
```

## Endpoints

### Eureka Dashboard
- **URL**: http://localhost:8085
- **Description**: Web UI for viewing registered services

### Actuator Endpoints
- **Health**: http://localhost:8085/actuator/health
- **Info**: http://localhost:8085/actuator/info
- **Metrics**: http://localhost:8085/actuator/metrics
- **All Endpoints**: http://localhost:8085/actuator

### API Endpoints
- **Info**: http://localhost:8085/info
- **Home**: http://localhost:8085/

## Health Checks

The application includes comprehensive health checks:
- Custom Eureka health indicator
- Actuator health endpoints
- Docker health checks
- Liveness and readiness probes

## Monitoring

### Metrics
- Micrometer metrics integration
- Prometheus metrics endpoint
- JVM and system metrics

### Logging
- Structured JSON logging (in Docker)
- Log rotation and retention
- Configurable log levels

### Tracing
- Zipkin distributed tracing
- Request correlation IDs
- Performance monitoring

## Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | 8085 |
| `eureka.server.enable-self-preservation` | Enable self-preservation mode | true |
| `eureka.server.renewal-percent-threshold` | Renewal threshold percentage | 0.85 |
| `eureka.server.eviction-interval-timer-in-ms` | Eviction interval | 30000 |

## Security

- Runs as non-root user in Docker
- Health checks for container orchestration
- Actuator endpoints secured appropriately
- Network security through container networking

## Performance Tuning

- Optimized JVM settings for containers
- Response cache configuration
- Renewal and eviction timers optimized
- G1 garbage collector for better performance

## Troubleshooting

### Common Issues

1. **Service not registering**
   - Check network connectivity
   - Verify service configuration
   - Check Eureka client configuration

2. **High CPU usage**
   - Check eviction interval settings
   - Monitor garbage collection
   - Review renewal threshold

3. **Memory issues**
   - Adjust JVM heap settings
   - Monitor registered services count
   - Check for memory leaks

### Logs Location
- Local: `logs/eureka-server.log`
- Docker: Container stdout/stderr

## Development

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── in/garvit/tasks/
│   │       ├── EurekaServerConfigurationApplication.java
│   │       ├── config/
│   │       │   └── EurekaServerConfig.java
│   │       └── controller/
│   │           ├── EurekaHealthIndicator.java
│   │           └── EurekaInfoController.java
│   └── resources/
│       ├── application.properties
│       ├── application-docker.properties
│       ├── logback-spring.xml
│       └── META-INF/
│           └── additional-spring-configuration-metadata.json
└── test/
    └── java/
```

### Building and Testing
```bash
# Run tests
./gradlew test

# Build application
./gradlew build

# Check dependencies
./gradlew dependencies
```

## License

This project is part of the Task Management System microservices architecture.

## Author

**garvitpathak27** - Initial work and maintenance