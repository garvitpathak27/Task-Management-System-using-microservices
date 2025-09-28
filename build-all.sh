#!/bin/bash

echo "Building all microservices..."

# Build Eureka Server
echo "Building Eureka Server..."
cd EurekaServerConfiguration
./gradlew clean build -x test
cd ..

# Build API Gateway
echo "Building API Gateway..."
cd APIGateWay
./gradlew clean build -x test
cd ..

# Build Task Service
echo "Building Task Service..."
cd TaskService
./gradlew clean build -x test
cd ..

# Build Task Submission Service
echo "Building Task Submission Service..."
cd TaskSubmissionService
./gradlew clean build -x test
cd ..

# Build Task User Service
echo "Building Task User Service..."
cd TaskUserService
./gradlew clean build -x test
cd ..

# If using UserService with Maven instead
# echo "Building User Service..."
# cd UserService
# ./mvnw clean package -DskipTests
# cd ..

echo "All services built successfully!"
