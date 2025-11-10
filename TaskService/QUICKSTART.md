# Task Service - Quick Start Guide

This guide will help you get the Task Service up and running in under 5 minutes.

---

## Prerequisites Checklist

Before starting, ensure you have:

- [ ] Java 17 or higher installed
- [ ] MongoDB installed and running
- [ ] Eureka Server running on port 8085
- [ ] User Service running on port 8081 (for full functionality)

---

## Quick Setup (3 Steps)

### Step 1: Start MongoDB
```bash
# Start MongoDB service
sudo systemctl start mongod

# Verify MongoDB is running
sudo systemctl status mongod

# Or if using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Step 2: Navigate to TaskService Directory
```bash
cd /path/to/Task-Management-System-using-microservices/TaskService
```

### Step 3: Run the Service
```bash
# Build and run
./gradlew clean bootRun

# Or build JAR and run
./gradlew clean build
java -jar build/libs/TaskService-0.0.1-SNAPSHOT.jar
```

---

## Verify Installation

### Test 1: Check Service Health
```bash
curl http://localhost:8082/tasks
```
**Expected Output:** `"Welcome to Task Service"`

### Test 2: Check Eureka Registration
Open browser: `http://localhost:8085`

Look for `TASK-SERVICE` in the registered instances.

### Test 3: Check Actuator Health
```bash
curl http://localhost:8082/actuator/health
```

---

## Quick Test with Sample Data

### 1. Get JWT Token from User Service
First, login via User Service to get a JWT token.

### 2. Create a Task (Admin Only)
```bash
curl -X POST http://localhost:8082/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My First Task",
    "description": "Testing task creation",
    "deadline": "2024-12-31T23:59:59",
    "tags": ["test", "demo"]
  }'
```

### 3. Get All Tasks
```bash
curl -X GET http://localhost:8082/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Get Tasks by Status
```bash
curl -X GET "http://localhost:8082/api/tasks?status=PENDING&sortByDeadline=asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Common Issues & Quick Fixes

### Issue 1: Port 8082 Already in Use
```bash
# Find the process
lsof -i :8082

# Kill it
kill -9 <PID>

# Or change port in application.properties
echo "server.port=8083" >> src/main/resources/application.properties
```

### Issue 2: MongoDB Connection Error
```bash
# Check if MongoDB is running
sudo systemctl status mongod

# If not, start it
sudo systemctl start mongod

# Check connection
mongo --eval "db.version()"
```

### Issue 3: Eureka Connection Error
```bash
# Check if Eureka is running on 8085
curl http://localhost:8085

# If not, start Eureka Server first
cd ../EurekaServerConfiguration
./gradlew bootRun
```

### Issue 4: User Service Connection Error
```bash
# Check if User Service is running on 8081
curl http://localhost:8081

# If not, start User Service
cd ../TaskUserService
./gradlew bootRun
```

---

## Development Mode

### Enable Hot Reload
```bash
# Run with DevTools for automatic restart
./gradlew bootRun --continuous
```

### Check Logs
```bash
# View application logs
tail -f logs/task-service.log

# Or if running in terminal, logs appear in console
```

### Debug Mode
```bash
# Run with debug enabled
./gradlew bootRun --debug-jvm
```

Then attach your IDE debugger to port 5005.

---

## Docker Quick Start

### Build Docker Image
```bash
# Build the application first
./gradlew clean build

# Build Docker image
docker build -t task-service:latest .
```

### Run Docker Container
```bash
docker run -d \
  -p 8082:8082 \
  -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/tasks \
  -e EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://host.docker.internal:8085/eureka \
  --name task-service \
  task-service:latest
```

### Check Container Logs
```bash
docker logs -f task-service
```

---

## Using Docker Compose (Recommended)

### Start All Services
```bash
# From the root directory
docker-compose up task-service

# Or start all services
docker-compose up
```

### Stop Services
```bash
docker-compose down
```

---

## Environment Configuration

### Development (Default)
```properties
# Uses localhost for all services
server.port=8082
spring.data.mongodb.uri=mongodb://localhost:27017/tasks
eureka.client.service-url.defaultZone=http://localhost:8085/eureka
```

### Docker
```properties
# Uses container names for service discovery
spring.data.mongodb.uri=mongodb://mongodb:27017/tasks
eureka.client.service-url.defaultZone=http://eureka-server:8085/eureka
```

---

## Testing the API

### Using cURL
See examples above or check `API_REFERENCE.md`

### Using Postman
1. Import the API endpoints from `API_REFERENCE.md`
2. Set up environment variables:
   - `BASE_URL`: http://localhost:8082
   - `JWT_TOKEN`: Your token from User Service
3. Set Authorization header: `Bearer {{JWT_TOKEN}}`

### Using HTTPie
```bash
# Install HTTPie
pip install httpie

# Test endpoint
http GET localhost:8082/tasks

# Create task
http POST localhost:8082/api/tasks \
  Authorization:"Bearer YOUR_TOKEN" \
  title="Test Task" \
  description="Testing with HTTPie" \
  deadline="2024-12-31T23:59:59"
```

---

## Next Steps

1. ✅ **Read the README**: Full documentation in `README.md`
2. ✅ **Check API Reference**: Complete API docs in `API_REFERENCE.md`
3. ✅ **Review Changes**: See what was improved in `CHANGELOG.md`
4. ✅ **Explore Code**: All classes have comprehensive JavaDoc

---

## Useful Commands

### Gradle Commands
```bash
# Clean build
./gradlew clean build

# Run tests
./gradlew test

# Run application
./gradlew bootRun

# Build without tests
./gradlew build -x test

# Show dependencies
./gradlew dependencies
```

### MongoDB Commands
```bash
# Connect to MongoDB
mongo

# Use tasks database
use tasks

# Show all tasks
db.Tasks.find().pretty()

# Count tasks
db.Tasks.count()

# Clear all tasks (careful!)
db.Tasks.deleteMany({})
```

### Service Commands
```bash
# Check if service is running
curl http://localhost:8082/tasks

# Check Eureka registration
curl http://localhost:8085/eureka/apps/TASK-SERVICE

# Check actuator health
curl http://localhost:8082/actuator/health

# Check actuator metrics
curl http://localhost:8082/actuator/metrics
```

---

## Getting Help

### Documentation
- **README.md** - Complete service documentation
- **API_REFERENCE.md** - API endpoint reference
- **CHANGELOG.md** - Version history
- **SUMMARY.md** - Code review summary

### Logs
Check logs for detailed error information:
```bash
# Application logs
tail -f logs/task-service.log

# Docker logs
docker logs -f task-service

# Gradle output
./gradlew bootRun --info
```

### Common Endpoints
- Health: http://localhost:8082/tasks
- Actuator: http://localhost:8082/actuator
- Eureka Dashboard: http://localhost:8085

---

## Ready to Go! 🚀

Your Task Service should now be running on **http://localhost:8082**

Try the health check:
```bash
curl http://localhost:8082/tasks
```

If you see `"Welcome to Task Service"`, you're all set! 🎉

---

**Happy Coding!** 💻
