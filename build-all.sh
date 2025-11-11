#!/bin/bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
GRADLE_SERVICES=(
	"EurekaServerConfiguration"
	"APIGateWay"
	"TaskService"
	"TaskSubmissionService"
	"TaskUserService"
)

echo "Building backend services..."
for service in "${GRADLE_SERVICES[@]}"; do
	echo " → ${service}"
	(
		cd "${ROOT_DIR}/${service}"
		./gradlew clean bootJar -x test
	)
done

echo "Building web UI..."
(
	cd "${ROOT_DIR}/task-management-ui"
	npm install
	npm run build
)

echo "All components built successfully."
