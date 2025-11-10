# Task Service

## Overview

The **Task Service** is a microservice component of the Task Management System that handles all task-related operations. It provides a RESTful API for creating, reading, updating, deleting, and managing tasks within the system.

## Purpose

This service is responsible for:
- **Task CRUD Operations**: Create, retrieve, update, and delete tasks
- **Task Assignment**: Assign tasks to specific users
- **Task Status Management**: Track task progression (PENDING → ASSIGNED → DONE)
- **Task Filtering & Sorting**: Filter tasks by status and sort by deadline or creation date
- **User Integration**: Communicates with User Service for authentication and authorization
- **Admin Controls**: Only administrators can create tasks

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.2.2
- **Language**: Java 17
- **Database**: MongoDB
- **Service Discovery**: Netflix Eureka Client
- **Inter-Service Communication**: OpenFeign
- **Distributed Tracing**: Zipkin with Micrometer
- **Build Tool**: Gradle

### Key Components

1. **Controllers**
   - `TaskController`: Handles all task-related REST endpoints
   - `HomeController`: Provides health check and welcome endpoints

2. **Services**
   - `TaskService`: Interface defining task operations
   - `TaskServiceImplementation`: Business logic implementation
   - `UserService`: Feign client for User Service integration

3. **Repository**
   - `TaskRepository`: MongoDB repository for task persistence

4. **Models**
   - `Task`: Main task entity
   - `UserDTO`: User data transfer object
   - `TaskStatus`: Enum for task states (PENDING, ASSIGNED, DONE)

5. **Exception Handling**
   - `GlobalExceptionHandler`: Centralized exception handling
   - `TaskNotFoundException`: Custom exception for task not found scenarios

## Database Schema

### Task Collection
```json
{
  "_id": "string",
  "title": "string",
  "description": "string",
  "imageUrl": "string",
  "assignedUserId": "string",
  "status": "PENDING | ASSIGNED | DONE",
  "deadline": "LocalDateTime",
  "createAt": "LocalDateTime",
  "tags": ["string"]
}
```

## API Endpoints

### Base URL
```
http://localhost:8082/api/tasks
```

### Endpoints

#### 1. Create Task (Admin Only)
```http
POST /api/tasks
Headers:
  Authorization: Bearer <JWT_TOKEN>
Body:
{
  "title": "Task Title",
  "description": "Task Description",
  "imageUrl": "https://example.com/image.png",
  "deadline": "2024-12-31T23:59:59",
  "tags": ["tag1", "tag2"]
}
Response: 201 CREATED
```

#### 2. Get Task by ID
```http
GET /api/tasks/{id}
Headers:
  Authorization: Bearer <JWT_TOKEN>
Response: 200 OK
```

#### 3. Get All Tasks
```http
GET /api/tasks?status=PENDING&sortByDeadline=asc&sortByCreatedAt=desc
Headers:
  Authorization: Bearer <JWT_TOKEN>
Query Parameters:
  - status: PENDING | ASSIGNED | DONE (optional)
  - sortByDeadline: asc | desc (optional)
  - sortByCreatedAt: asc | desc (optional)
Response: 200 OK
```

#### 4. Get Assigned User Tasks
```http
GET /api/tasks/user?status=ASSIGNED&sortByDeadline=asc
Headers:
  Authorization: Bearer <JWT_TOKEN>
Query Parameters:
  - status: PENDING | ASSIGNED | DONE (optional)
  - sortByDeadline: asc | desc (optional)
  - sortByCreatedAt: asc | desc (optional)
Response: 200 OK
```

#### 5. Assign Task to User
```http
PUT /api/tasks/{taskId}/user/{userId}/assigned
Headers:
  Authorization: Bearer <JWT_TOKEN>
Response: 200 OK
```

#### 6. Update Task
```http
PUT /api/tasks/{id}
Headers:
  Authorization: Bearer <JWT_TOKEN>
Body:
{
  "title": "Updated Title",
  "description": "Updated Description",
  "status": "ASSIGNED",
  "deadline": "2024-12-31T23:59:59"
}
Response: 200 OK
```

#### 7. Delete Task
```http
DELETE /api/tasks/{id}
Response: 204 NO CONTENT
```

#### 8. Complete Task
```http
PUT /api/tasks/{id}/complete
Response: 200 OK
```

#### 9. Health Check
```http
GET /tasks
Response: 200 OK - "Welcome to Task Service"
```

## Configuration

### application.properties
```properties
# Server Configuration
server.port=8082

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/tasks

# Application Name
spring.application.name=TASK-SERVICE

# Eureka Client Configuration
eureka.instance.prefer-ip-address=true
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://localhost:8085/eureka

# Zipkin Tracing Configuration
spring.zipkin.base-url=http://localhost:9411
spring.sleuth.sampler.probability=1.0
```

## Prerequisites

Before running the Task Service, ensure you have:

1. **Java 17** or higher installed
2. **MongoDB** installed and running on `localhost:27017`
3. **Eureka Server** running on `http://localhost:8085`
4. **User Service** running on `http://localhost:8081` (for Feign client)
5. **Zipkin Server** (optional) running on `http://localhost:9411` for distributed tracing

## How to Run

### Method 1: Using Gradle Wrapper

#### 1. Navigate to TaskService directory
```bash
cd TaskService
```

#### 2. Build the application
```bash
./gradlew clean build
```

#### 3. Run the application
```bash
./gradlew bootRun
```

### Method 2: Using JAR file

#### 1. Build the JAR
```bash
./gradlew clean build
```

#### 2. Run the JAR
```bash
java -jar build/libs/TaskService-0.0.1-SNAPSHOT.jar
```

### Method 3: Using Docker

#### 1. Build the Docker image
```bash
docker build -t task-service:latest .
```

#### 2. Run the Docker container
```bash
docker run -p 8082:8082 \
  -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/tasks \
  -e EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://host.docker.internal:8085/eureka \
  task-service:latest
```

### Method 4: Using Docker Compose (Recommended)

From the root directory of the project:
```bash
docker-compose up task-service
```

## Verification

Once the service is running, verify it's working:

### 1. Check Service Health
```bash
curl http://localhost:8082/tasks
```
Expected response: `"Welcome to Task Service"`

### 2. Check Eureka Registration
Visit `http://localhost:8085` and verify that `TASK-SERVICE` is registered.

### 3. Check MongoDB Connection
Ensure the MongoDB database `tasks` is created and accessible.

## Integration with Other Services

### User Service Integration
The Task Service communicates with the User Service using OpenFeign:
- **URL**: `http://localhost:8081`
- **Endpoint**: `/api/users/profile`
- **Purpose**: Fetch user profile information for authentication and authorization

### Eureka Service Discovery
The Task Service registers itself with Eureka Server for service discovery:
- **Service Name**: `TASK-SERVICE`
- **Eureka Server**: `http://localhost:8085/eureka`

### Zipkin Tracing
Distributed tracing is enabled for monitoring:
- **Zipkin URL**: `http://localhost:9411`
- **Sampling Probability**: 100% (all requests are traced)

## Authentication & Authorization

All endpoints (except the health check) require JWT authentication:
- **Header**: `Authorization: Bearer <JWT_TOKEN>`
- **Admin Role**: Required for creating tasks (`ROLE_ADMIN`)
- **User Verification**: JWT is validated via User Service

## Error Handling

The service includes comprehensive error handling:

### Common Error Responses

#### 400 Bad Request
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Invalid request parameters",
  "status": 400,
  "error": "Bad Request"
}
```

#### 401 Unauthorized
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "JWT required...",
  "status": 401,
  "error": "Unauthorized"
}
```

#### 404 Not Found
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Task not found with id: 12345",
  "status": 404,
  "error": "Task Not Found"
}
```

#### 500 Internal Server Error
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Internal server error",
  "status": 500,
  "error": "Internal Server Error"
}
```

## Task Lifecycle

1. **PENDING**: Task is created (by admin only)
2. **ASSIGNED**: Task is assigned to a user
3. **DONE**: Task is marked as completed

## Development

### Project Structure
```
TaskService/
├── src/
│   ├── main/
│   │   ├── java/in/garvit/tasks/
│   │   │   ├── TaskServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   └── TaskController.java
│   │   │   ├── service/
│   │   │   │   ├── TaskService.java
│   │   │   │   ├── TaskServiceImplementation.java
│   │   │   │   └── UserService.java
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.java
│   │   │   ├── taskModel/
│   │   │   │   ├── Task.java
│   │   │   │   └── UserDTO.java
│   │   │   ├── enums/
│   │   │   │   └── TaskStatus.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       └── TaskNotFoundException.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/in/garvit/tasks/
│           └── TaskServiceApplicationTests.java
├── build.gradle
├── Dockerfile
└── README.md
```

### Building from Source

```bash
# Clone the repository
git clone <repository-url>

# Navigate to TaskService
cd TaskService

# Build the project
./gradlew clean build

# Run tests
./gradlew test

# Run the application
./gradlew bootRun
```

## Testing

### Run Unit Tests
```bash
./gradlew test
```

### Test Coverage
```bash
./gradlew test jacocoTestReport
```

## Troubleshooting

### Common Issues

#### 1. MongoDB Connection Failed
**Error**: `Connection refused to MongoDB`
**Solution**: Ensure MongoDB is running:
```bash
# Start MongoDB
sudo systemctl start mongod

# Check status
sudo systemctl status mongod
```

#### 2. Eureka Registration Failed
**Error**: `Cannot register with Eureka`
**Solution**: Ensure Eureka Server is running on port 8085

#### 3. User Service Communication Failed
**Error**: `Feign client error`
**Solution**: Ensure User Service is running on port 8081

#### 4. Port Already in Use
**Error**: `Port 8082 is already in use`
**Solution**: Change the port in `application.properties` or kill the process:
```bash
# Find process using port 8082
lsof -i :8082

# Kill the process
kill -9 <PID>
```

## Performance Considerations

- **Database Indexing**: Consider adding indexes on frequently queried fields (e.g., `assignedUserId`, `status`)
- **Caching**: Implement caching for frequently accessed tasks
- **Pagination**: Add pagination for large result sets
- **Connection Pooling**: MongoDB connection pool is managed by Spring Data

## Security Considerations

- All endpoints require JWT authentication
- Only admins can create tasks
- User validation is performed via User Service
- Sensitive data should be encrypted at rest
- HTTPS should be used in production

## Monitoring

### Health Check Endpoint
```bash
curl http://localhost:8082/actuator/health
```

### Metrics Endpoint
```bash
curl http://localhost:8082/actuator/metrics
```

### Zipkin Dashboard
Access Zipkin UI at: `http://localhost:9411`

## Future Enhancements

- [ ] Add pagination support for task listings
- [ ] Implement task comments/notes feature
- [ ] Add task priority levels
- [ ] Implement recurring tasks
- [ ] Add file attachments to tasks
- [ ] Implement task notifications
- [ ] Add task templates
- [ ] Implement task categories
- [ ] Add advanced search capabilities

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is part of the Task Management System microservices architecture.

## Contact

**Author**: Garvit Pathak (garvitpathak27)
**Email**: [Your Email]
**GitHub**: [Your GitHub Profile]

---

**Note**: This service is part of a larger microservices ecosystem. Ensure all dependent services are running for full functionality.
