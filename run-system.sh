#!/bin/bash

echo "Starting Task Management System..."

# Build all services first
./build-all.sh

# Start all containers with Docker Compose
echo "Starting Docker containers..."
docker-compose up --build -d

echo "System is starting up..."
echo "Services will be available at:"
echo "- Eureka Server: http://localhost:8085"
echo "- API Gateway: http://localhost:8090"
echo "- User Service: http://localhost:8081"
echo "- Task Service: http://localhost:8082"
echo "- Task Submission Service: http://localhost:8083"
echo "- MongoDB: localhost:27017"
echo "- Zipkin: http://localhost:9411"

echo ""
echo "Wait for all services to start (about 2-3 minutes)"
echo "Check status with: docker-compose ps"
echo "View logs with: docker-compose logs -f [service-name]"
