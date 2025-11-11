#!/bin/bash

# Complete System Startup Script
# This script starts both the backend microservices and the React frontend

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "=========================================="
echo "   Task Management System - Full Startup"
echo "=========================================="
echo

# Detect Docker Compose command
if command -v docker &>/dev/null && docker compose version &>/dev/null; then
    DOCKER_COMPOSE_CMD=(docker compose)
elif command -v docker-compose &>/dev/null; then
    DOCKER_COMPOSE_CMD=(docker-compose)
else
    echo "❌ Error: Docker Compose is not installed." >&2
    exit 1
fi

# Step 1: Build and start backend services
echo "📦 Step 1: Building and starting backend microservices..."
"${ROOT_DIR}/build-all.sh"

echo
echo "🚀 Starting Docker containers..."
(cd "${ROOT_DIR}" && "${DOCKER_COMPOSE_CMD[@]}" up -d)

echo
echo "⏳ Waiting for services to be healthy (30 seconds)..."
sleep 30

# Step 2: Check if npm is installed
echo
echo "📦 Step 2: Starting React frontend..."
if ! command -v npm &>/dev/null; then
    echo "⚠️  Warning: npm is not installed. Please install Node.js and npm to run the frontend."
    echo "   Visit: https://nodejs.org/"
    echo
    echo "   For now, only backend services are running."
else
    cd "${ROOT_DIR}/task-management-ui"
    
    # Install dependencies if node_modules doesn't exist
    if [ ! -d "node_modules" ]; then
        echo "📥 Installing frontend dependencies..."
        npm install
    fi
    
    # Start the frontend in the background
    echo "🚀 Starting React development server..."
    npm start &
    FRONTEND_PID=$!
    
    echo
    echo "✅ Frontend is starting... (PID: $FRONTEND_PID)"
    echo "   It will be available at http://localhost:3000 in about 10-15 seconds"
fi

echo
echo "=========================================="
echo "✅ System Startup Complete!"
echo "=========================================="
echo
echo "📊 Service URLs:"
echo "   • Frontend (React):        http://localhost:3000"
echo "   • API Gateway:             http://localhost:8090"
echo "   • Eureka Dashboard:        http://localhost:8085"
echo "   • Zipkin Tracing:          http://localhost:9411"
echo "   • User Service:            http://localhost:8081"
echo "   • Task Service:            http://localhost:8082"
echo "   • Submission Service:      http://localhost:8083"
echo
echo "🔧 Useful Commands:"
echo "   • Check service status:    ${DOCKER_COMPOSE_CMD[*]} ps"
echo "   • View logs:               ${DOCKER_COMPOSE_CMD[*]} logs -f [service-name]"
echo "   • Stop all services:       ./stop-all.sh"
echo
echo "📝 Note: Frontend runs in the background. To stop it:"
if [ -n "${FRONTEND_PID:-}" ]; then
    echo "   kill $FRONTEND_PID"
fi
echo

