# Task Submission Service

## Overview
Task Submission Service manages the lifecycle of task submissions within the Task Management System. It lets assignees upload their work for review, tracks submission status changes, and coordinates with the task catalog so approved work marks the underlying task as complete.

## Architecture Summary
- **Frameworks:** Spring Boot 3, Spring Cloud OpenFeign, Spring Data MongoDB, Netflix Eureka client
- **Database:** MongoDB collection `taskSubmission`
- **Remote Dependencies:**
  - `TASK-SERVICE` (task metadata and status updates)
  - `USER-SERVICE` (authenticated user profiles)
- **Key Modules:**
  - `controller` – REST endpoints for CRUD-style submission interactions
  - `service` – domain rules, inter-service calls, validation, status transitions
  - `repository` – MongoDB access with compound indexes for fast lookup
  - `submissionModel` – persistence entities and DTOs for remote services
  - `exception` – typed errors plus consistent REST error responses

## Setup Instructions
1. **Prerequisites**
   - Java 17+
   - MongoDB running locally (`mongodb://localhost:27017/submissions` by default)
   - Optional: Eureka server on `http://localhost:8085/eureka` for service discovery
2. **Install dependencies**
   ```bash
   cd TaskSubmissionService
   ./gradlew clean build
   ```
3. **Run the service**
   ```bash
   ./gradlew bootRun
   ```
   Environment may be configured via `src/main/resources/application.properties` or external properties.
4. **Run automated tests**
   ```bash
   ./gradlew test
   ```

## API Endpoints
| Method | Path | Description |
|--------|------|-------------|
| `GET`  | `/submissions` | List all submissions (legacy UI compatibility) |
| `POST` | `/api/submissions` | Create a submission for the authenticated user (expects `{ "taskId", "content" }`) |
| `GET`  | `/api/submissions/{submissionId}` | Retrieve a specific submission |
| `GET`  | `/api/submissions` | List submissions (sortable newest first) |
| `GET`  | `/api/submissions/task/{taskId}` | List submissions for a given task |
| `PUT`  | `/api/submissions/{submissionId}` | Update status/content (admins may approve/reject, owners may edit content while pending) |

Responses are JSON objects using `TaskSubmissionResponse`, which surfaces `id`, `taskId`, `userId`, `status`, `content`, `createdAt`, and `updatedAt` (ISO timestamps). Status values are lower-case strings (`pending`, `approved`, `rejected`).

## Changes Summary
- Replaced stubbed service layer with a fully implemented `SubServiceImp` featuring validation, permission checks, remote task integration, and approval workflow.
- Introduced DTOs (`TaskSubmissionRequest`, `TaskSubmissionResponse`, `SubmissionStatusUpdateRequest`) and modernized controllers to return typed responses.
- Hardened persistence model (`TaskSubmission`) with auditing timestamps, compound indexes, enums (`SubmissionStatus`, `TaskStatus`), and utility helpers for status/content updates.
- Added dedicated exceptions plus `RestExceptionHandler` to surface consistent HTTP errors for duplicates, invalid state, and authorization failures.
- Enabled Mongo auditing, refined Feign clients to leverage Eureka discovery, and documented the service with setup instructions, endpoint catalogue, and test guidance.
- Created unit tests for core service behaviours, ensuring duplication safeguards, access control, and remote error handling remain covered.
