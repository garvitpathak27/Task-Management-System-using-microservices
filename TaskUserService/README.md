# Task User Service

## Overview
Task User Service is the identity and profile microservice for the Task Management platform. It handles user onboarding, authentication, profile lookups, and directory queries. The service persists user data in MongoDB, issues JWTs for stateless security, discovers peers through Eureka, and publishes tracing data to Zipkin.

## Tech Stack
- Spring Boot 3.2 (Java 17) with Gradle build
- Spring Security with JWT + Resilience4j circuit breakers
- MongoDB for user persistence via Spring Data
- Eureka client for service discovery
- Zipkin (Micrometer tracing bridge) for distributed tracing

## Responsibilities
- Register new users, hashing credentials before persistence
- Authenticate users and issue signed JWT access tokens
- Resolve user profiles from bearer tokens
- Provide directory APIs for other microservices
- Expose health/readiness probes for orchestration

## External Dependencies
| Service | Purpose | How to start locally |
| --- | --- | --- |
| MongoDB | Primary datastore (`user` collection). | `docker run -d --name task-mongo -p 27017:27017 mongo:7.0` or install MongoDB 7.x manually. |
| Eureka | Service discovery (default `http://localhost:8085/eureka`). | `./gradlew bootRun` from `EurekaServerConfiguration` or `docker compose up eureka-server`. |
| Zipkin | Distributed tracing (default `http://localhost:9411`). | `docker compose up zipkin` or run Zipkin jar. |

> A full environment (MongoDB, Zipkin, Eureka, and microservices) can be started from the repository root with `docker compose up -d mongodb zipkin eureka-server user-service`.

## Getting Started
1. **Prerequisites**
	- JDK 17+
	- Docker (optional but recommended for MongoDB/Eureka/Zipkin)
	- Gradle wrapper is bundled (`./gradlew`)
2. **Environment configuration**
	```bash
	cd TaskUserService
	cp .env.example .env
	# update .env with secure values (especially TASK_JWT_SECRET)
	```
	Spring automatically reads the variables thanks to `application.properties` placeholders.
3. **Install & run dependencies**
	- **MongoDB**: ensure a database is available at `MONGODB_URI`. Example using Docker:
	  ```bash
	  docker run -d --name task-mongo -p 27017:27017 mongo:7.0
	  ```
	- **Zipkin & Eureka**: start locally or via Compose. From repo root:
	  ```bash
	  docker compose up -d zipkin eureka-server
	  ```
4. **Run the service locally**
	```bash
	./gradlew clean bootRun
	```
	The API listens on `http://localhost:${SERVER_PORT:-8081}`. Make sure `.env` is loaded in your shell or an IDE run configuration.
5. **Run the service with Docker Compose**
	```bash
	docker compose up -d user-service
	```
	Compose automatically wires environment variables declared for the service profile.
6. **Execute tests**
	```bash
	./gradlew test
	```

## Environment Variables
The service is fully configurable via environment variables; see `.env.example` for a ready-to-copy template.

| Variable | Default | Description |
| --- | --- | --- |
| `SERVER_PORT` | `8081` | HTTP port exposed by the service. |
| `MONGODB_URI` | `mongodb://localhost:27017/userData` | MongoDB connection string. |
| `EUREKA_SERVER_URI` | `http://localhost:8085/eureka` | Eureka discovery endpoint. |
| `ZIPKIN_BASE_URL` | `http://localhost:9411` | Zipkin server URL. |
| `ZIPKIN_SAMPLER_PROBABILITY` | `1.0` | Sampling ratio for tracing. |
| `TASK_JWT_SECRET` | _required_ (default placeholder) | Secret used to sign JWTs (must be ≥ 32 chars). |
| `TASK_JWT_ACCESS_TOKEN_TTL_MINUTES` | `1440` | JWT access token lifetime in minutes. |
| `TASK_ALLOWED_ORIGINS` | `http://localhost:3000` | Comma-separated list of allowed CORS origins. |
| `TASK_ALLOWED_HEADERS` | `*` | Allowed CORS headers. |
| `TASK_ALLOWED_METHODS` | `GET,POST,PUT,PATCH,DELETE,OPTIONS` | Allowed CORS methods. |
| `TASK_ALLOW_CREDENTIALS` | `true` | Whether cross-site credentials are allowed. |
| `TASK_CORS_MAX_AGE` | `3600` | CORS cache duration in seconds. |
| `LOG_LEVEL_ROOT` | `INFO` | Default logging level. |
| `LOG_LEVEL_APP` | `INFO` | Package-specific logging level. |

## API Surface
| Method | Path | Auth | Description |
| --- | --- | --- | --- |
| `POST` | `/auth/signup` | ❌ | Register a new user and auto-sign in (returns JWT). |
| `POST` | `/auth/signin` | ❌ | Authenticate with email/password and receive a JWT. |
| `GET` | `/api/users/profile` | ✅ | Retrieve the profile of the caller based on JWT. |
| `GET` | `/api/users/{userId}` | ✅ | Fetch a specific user by id. |
| `GET` | `/api/users` | ✅ | List all users (sensitive fields removed). |
| `GET` | `/api/users/health` | ❌ | Lightweight readiness check. |
| `GET` | `/actuator/health` | ❌ | Spring Boot actuator health endpoint. |

Example signup request:

```bash
curl -X POST http://localhost:8081/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{
		  "fullName": "Ada Lovelace",
		  "email": "ada@example.com",
		  "password": "Sup3rSecret!",
		  "mobile": "1234567890"
		}'
```

Use the `jwt` field returned from signup/signin as `Authorization: Bearer <token>` for protected routes.

## Error Handling & Resilience
- Centralised `GlobalExceptionHandler` converts validation, authentication, and domain errors into consistent `ApiResponse` payloads.
- Sensitive fields (password hashes) are removed from outbound responses via dedicated `UserResponse` DTOs.
- Resilience4j circuit breakers wrap user lookups and authentication flows; fallbacks surface `503 Service Unavailable` responses when tripped.
- JWT validation happens in a single filter using configurable secrets and token TTLs; invalid or missing tokens no longer poison the security context.

## Troubleshooting
- **Mongo connection refused**: ensure MongoDB is running and `MONGODB_URI` matches credentials/host. With Docker, verify `docker ps` shows `task-mongo` healthy.
- **JWT secret errors**: the application now validates that `TASK_JWT_SECRET` is at least 32 characters. Update `.env` before starting the service.
- **401 after login**: confirm the `Authorization` header uses the `Bearer` prefix and the token has not expired (see configured TTL).
- **Eureka/Zipkin unavailable**: startup succeeds without them, but resilience metrics degrade. Adjust `.env` or bring the services up via Compose.
- **CORS failures**: update `TASK_ALLOWED_ORIGINS` with the exact origin(s) of the frontend (comma separated).
- **Port conflicts**: change `SERVER_PORT` in your `.env` and restart (`./gradlew bootRun`).

## Recent Fixes & Refactors
- Replaced hard-coded JWT constants with validated configuration properties and injected `JwtProvider` bean.
- Added stateless security filter chain with configurable CORS and a single `JwtTokenValidator` instance.
- Introduced `UserResponse` DTOs to strip sensitive data and harmonised HTTP status codes (200/503 instead of 202/500).
- Implemented global exception handling and structured logging across controllers and services.
- Parameterised configuration through environment variables, shipped `.env.example`, and refreshed documentation.

