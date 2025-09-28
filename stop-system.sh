#!/bin/bash

echo "Stopping Task Management System..."
docker-compose down

echo "To remove volumes as well (will delete data):"
echo "docker-compose down -v"

