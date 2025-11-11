#!/bin/bash

# Complete System Shutdown Script
# This script stops both the backend microservices and the React frontend

set -euo pipefail

echo "=========================================="
echo "   Task Management System - Shutdown"
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

# Stop frontend if running
echo "🛑 Stopping React frontend..."
pkill -f "react-scripts start" || echo "   Frontend was not running"

# Stop backend services
echo
echo "🛑 Stopping backend microservices..."
"${DOCKER_COMPOSE_CMD[@]}" down

echo
echo "=========================================="
echo "✅ All services stopped successfully!"
echo "=========================================="
echo
echo "💡 To also remove data volumes (MongoDB data), run:"
echo "   ${DOCKER_COMPOSE_CMD[*]} down -v"
echo

