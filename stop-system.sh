#!/bin/bash

set -euo pipefail

if command -v docker &>/dev/null && docker compose version &>/dev/null; then
	DOCKER_COMPOSE_CMD=(docker compose)
elif command -v docker-compose &>/dev/null; then
	DOCKER_COMPOSE_CMD=(docker-compose)
else
	echo "Error: Docker Compose is not installed." >&2
	exit 1
fi

echo "Stopping Task Management System..."
"${DOCKER_COMPOSE_CMD[@]}" down

echo "To remove volumes as well (will delete data):"
echo "${DOCKER_COMPOSE_CMD[*]} down -v"

