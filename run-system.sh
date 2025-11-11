#!/bin/bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting Task Management System..."

"${ROOT_DIR}/build-all.sh"

if command -v docker &>/dev/null && docker compose version &>/dev/null; then
	DOCKER_COMPOSE_CMD=(docker compose)
elif command -v docker-compose &>/dev/null; then
	DOCKER_COMPOSE_CMD=(docker-compose)
else
	echo "Error: Docker Compose is not installed." >&2
	exit 1
fi

echo "Starting Docker containers..."
(cd "${ROOT_DIR}" && "${DOCKER_COMPOSE_CMD[@]}" up --build -d)

echo "System is starting up..."
echo "Services will be available at:"
echo "- Eureka Server: http://localhost:8085"
echo "- API Gateway: http://localhost:8090"
echo "- User Service: http://localhost:8081"
echo "- Task Service: http://localhost:8082"
echo "- Task Submission Service: http://localhost:8083"
echo "- MongoDB: localhost:27017"
echo "- Zipkin: http://localhost:9411"

echo
echo "Wait for all services to start (about 2-3 minutes)"
echo "Check status with: ${DOCKER_COMPOSE_CMD[*]} ps"
echo "View logs with: ${DOCKER_COMPOSE_CMD[*]} logs -f <service>"
