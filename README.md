# Task Management System (Microservices)

A Spring Cloud microservice ecosystem for managing tasks with a React front-end. An API Gateway exposes the platform through a single port, Eureka provides service discovery, and MongoDB persists user, task, and submission data. The solution can be run locally with Gradle/npm or containerised end-to-end with Docker Compose.

---

## Overview

This project implements a Task Management system using a microservices architecture. The system consists of three main microservices plus infrastructure components:

- **TaskUserService** – authentication, JWT issuance, profile and admin endpoints.
- **TaskService** – task CRUD, assignment and completion workflows.
- **TaskSubmissionService** – submission lifecycle and coordination with other services via OpenFeign.
- **EurekaServerConfiguration** – service registry for discovery and health status.
- **APIGateway** – Spring Cloud Gateway with resilience policies and CORS/fallback handling.
- **task-management-ui** – React SPA consuming the gateway APIs.

Additional runtime services in Docker Compose:
- **MongoDB** (Docker)
- **Zipkin** (Docker) for tracing

---

## Tech stack

- Java 17, Spring Boot 3.2, Spring Cloud 2023.0
- Gradle 8
- React 18 (Create React App)
- Node.js 18+, npm 9+
- MongoDB 6
- Docker & Docker Compose (Docker Engine 24+, Compose v2 recommended)
- Spring Security, Resilience4j, OpenFeign, Micrometer/Zipkin


## Repository layout (high level)

```
APIGateWay/                 # Spring Cloud Gateway service
EurekaServerConfiguration/  # Eureka discovery server
TaskUserService/            # Authentication / User management service
TaskService/                # Task domain microservice
TaskSubmissionService/      # Submission microservice
task-management-ui/         # React single page application
build-all.sh                # Builds all services and UI
run-system.sh               # Builds and launches full stack with Compose
stop-system.sh              # Stops the Compose stack
docker-compose.yml         # Compose orchestration for services + MongoDB + Zipkin
```

---

## Features (selected)

### UserService
- `POST /auth/signup` — user registration
- `POST /auth/signin` — authentication (returns JWT)
- `GET /api/user/profile` — get user profile

### TaskService
- `POST /api/tasks/create` — create new task (admin only)
- `GET /api/tasks/{taskId}` — get task details
- `PUT /api/tasks/{taskId}/assign/{userId}` — assign task to user (admin only)

### TaskSubmissionService
- `POST /api/submissions/submit` — submit completed task (authenticated users)
- `GET /api/submissions/{submissionId}` — get submission details

---

## Configuration

All Spring services accept environment overrides (via environment variables or `application-*.properties`) for values such as:

| Variable | Applies to | Purpose | Default (local) |
|---|---:|---|---:|
| `SERVER_PORT` | All Spring services | HTTP port override | service-specific |
| `SPRING_APP_NAME` | All services | Service identifier / registration name | - |
| `EUREKA_SERVER_URI` | Gateway & microservices | Eureka discovery endpoint | `http://localhost:8085/eureka` |
| `MONGODB_URI` | Backends | MongoDB connection string | `mongodb://localhost:27017/<db>` |
| `ZIPKIN_BASE_URL` | All services | Zipkin tracing endpoint | `http://localhost:9411` |
| `ZIPKIN_SAMPLER_PROBABILITY` | All services | Tracing sampling rate | `1.0` |

Each service also exposes domain-specific properties (JWT secrets, CORS allowed origins, etc.) in its `application.properties`/`application-docker.properties` files. Defaults are safe for local usage but **must** be overridden for production.

---

## Prerequisites / System requirements

- JDK 17+
- Node.js 18+, npm 9+
- Docker Engine 24+ and Docker Compose v2 (or docker-compose v1)
- 8 GB RAM recommended when running full stack locally

---

## Setup & Running

### Option 1 — Full stack via Docker Compose (recommended for quick end-to-end runs)

1. (Optional) Create a Docker volume for MongoDB persistence:
   ```bash
   docker volume create mongo-data
   ```
2. Build and run everything (the `run-system.sh` helper can be used if present):
   ```bash
   ./run-system.sh
   # or
   docker compose up --build
   ```
3. Verify services are registered in Eureka (default port shown by the compose file):
   - Eureka dashboard: `http://localhost:8085`
   - API Gateway health: `http://localhost:8090/actuator/health`
   - Zipkin UI: `http://localhost:9411`
   - React dev UI: `http://localhost:3000` (if using `npm start` instead of build)

Stop everything:
```bash
./stop-system.sh
# or
docker compose down
```

To remove persisted MongoDB data:
```bash
docker volume rm mongo-data
```

### Option 2 — Run services locally (development)

1. Start MongoDB locally or via Docker:
   ```bash
   docker run -p 27017:27017 --name mongo -d mongo:6.0
   ```
2. Start Eureka server:
   ```bash
   cd EurekaServerConfiguration
   ./gradlew bootRun
   ```
3. Start services in separate terminals:
   ```bash
   cd TaskUserService && ./gradlew bootRun
   cd TaskService && ./gradlew bootRun
   cd TaskSubmissionService && ./gradlew bootRun
   cd APIGateWay && ./gradlew bootRun
   ```
4. Start React UI (dev mode):
   ```bash
   cd task-management-ui
   npm install
   npm start
   ```

When running behind the gateway, point the UI to the gateway base URL (example `.env`):
```
REACT_APP_API_BASE_URL=http://localhost:8090
```

---

## Build

### One-step build (bundles backends and UI)

```bash
./build-all.sh
```

### Manual builds

- Backends: inside each service directory
```bash
./gradlew clean bootJar -x test
```
- UI
```bash
cd task-management-ui
npm install
npm run build
```

---

## Troubleshooting

- **MongoDB health check fails**: ensure your host CPU supports AVX (required for MongoDB 6), or run a remote MongoDB instance and set `MONGODB_URI` accordingly.
- **Port conflicts**: adjust `SERVER_PORT` environment variable for services.
- **Gateway CORS errors**: set appropriate `GATEWAY_ALLOWED_ORIGIN_*` and `TASK_ALLOWED_ORIGINS` env vars to match your front-end origin(s).
- **Zipkin unreachable**: either run Zipkin container or comment out `ZIPKIN_BASE_URL` in configs to disable tracing.

---

## Verification checklist

- Eureka dashboard shows `USER-SERVICE`, `TASK-SERVICE`, and `TASK-SUBMISSION` as `UP`.
- Gateway health endpoint returns `{"status":"UP"}`.
- Service actuator health endpoints are available (e.g. `http://localhost:8081/actuator/health`, `:8082`, `:8083`).
- MongoDB collections: `users`, `tasks`, `submissions`.

---

## Notes

- Admin-only endpoints are protected by role checks (create/assign tasks).
- OpenFeign is used for inter-service HTTP clients.
- Production deployments should rotate secrets and secure all JWT keys / database credentials.

---

## Contribution

Contributions are welcome — open an issue or submit a PR.

## License

This project is licensed under the MIT License.

---

*Happy shipping! 🚀*
