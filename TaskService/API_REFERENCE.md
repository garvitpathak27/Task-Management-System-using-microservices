# Task Service API Quick Reference

## Base URL
```
http://localhost:8082
```

## Authentication
All endpoints (except health check) require JWT authentication in the header:
```
Authorization: Bearer <your_jwt_token>
```

---

## Endpoints Summary

| Method | Endpoint | Description | Auth | Admin Only |
|--------|----------|-------------|------|------------|
| GET | `/tasks` | Health check | No | No |
| POST | `/api/tasks` | Create task | Yes | Yes |
| GET | `/api/tasks` | Get all tasks | Yes | No |
| GET | `/api/tasks/{id}` | Get task by ID | Yes | No |
| GET | `/api/tasks/user` | Get user's assigned tasks | Yes | No |
| PUT | `/api/tasks/{id}` | Update task | Yes | No |
| PUT | `/api/tasks/{id}/user/{userId}/assigned` | Assign task to user | Yes | No |
| PUT | `/api/tasks/{id}/complete` | Mark task as complete | Yes | No |
| DELETE | `/api/tasks/{id}` | Delete task | Yes | No |

---

## Detailed API Documentation

### 1. Health Check
Check if service is running.

**Request:**
```http
GET /tasks
```

**Response:**
```json
"Welcome to Task Service"
```

---

### 2. Create Task (Admin Only)
Create a new task. Only users with ROLE_ADMIN can create tasks.

**Request:**
```http
POST /api/tasks
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Implement new feature",
  "description": "Implement the new authentication feature",
  "imageUrl": "https://example.com/image.png",
  "deadline": "2024-12-31T23:59:59",
  "tags": ["backend", "security", "priority-high"]
}
```

**Response (201 CREATED):**
```json
{
  "id": "65f9a1234567890abcdef123",
  "title": "Implement new feature",
  "description": "Implement the new authentication feature",
  "imageUrl": "https://example.com/image.png",
  "assignedUserId": null,
  "status": "PENDING",
  "deadline": "2024-12-31T23:59:59",
  "createAt": "2024-11-10T10:30:00",
  "tags": ["backend", "security", "priority-high"]
}
```

---

### 3. Get All Tasks
Retrieve all tasks with optional filtering and sorting.

**Request:**
```http
GET /api/tasks?status=PENDING&sortByDeadline=asc
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `status` (optional): Filter by status - `PENDING`, `ASSIGNED`, or `DONE`
- `sortByDeadline` (optional): Sort by deadline - `asc` or `desc`
- `sortByCreatedAt` (optional): Sort by creation date - `asc` or `desc`

**Response (200 OK):**
```json
[
  {
    "id": "65f9a1234567890abcdef123",
    "title": "Implement new feature",
    "description": "Implement the new authentication feature",
    "imageUrl": "https://example.com/image.png",
    "assignedUserId": null,
    "status": "PENDING",
    "deadline": "2024-12-31T23:59:59",
    "createAt": "2024-11-10T10:30:00",
    "tags": ["backend", "security"]
  },
  {
    "id": "65f9a1234567890abcdef456",
    "title": "Fix bug in payment module",
    "description": "Resolve the payment gateway timeout issue",
    "imageUrl": null,
    "assignedUserId": null,
    "status": "PENDING",
    "deadline": "2024-11-15T18:00:00",
    "createAt": "2024-11-10T11:00:00",
    "tags": ["bug", "payment"]
  }
]
```

---

### 4. Get Task by ID
Retrieve a specific task by its ID.

**Request:**
```http
GET /api/tasks/65f9a1234567890abcdef123
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "id": "65f9a1234567890abcdef123",
  "title": "Implement new feature",
  "description": "Implement the new authentication feature",
  "imageUrl": "https://example.com/image.png",
  "assignedUserId": "user123",
  "status": "ASSIGNED",
  "deadline": "2024-12-31T23:59:59",
  "createAt": "2024-11-10T10:30:00",
  "tags": ["backend", "security"]
}
```

**Error Response (404 NOT FOUND):**
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Task not found with id: 65f9a1234567890abcdef123",
  "status": 404,
  "error": "Task Not Found"
}
```

---

### 5. Get User's Assigned Tasks
Retrieve all tasks assigned to the authenticated user.

**Request:**
```http
GET /api/tasks/user?status=ASSIGNED&sortByDeadline=asc
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `status` (optional): Filter by status - `PENDING`, `ASSIGNED`, or `DONE`
- `sortByDeadline` (optional): Sort by deadline - `asc` or `desc`
- `sortByCreatedAt` (optional): Sort by creation date - `asc` or `desc`

**Response (200 OK):**
```json
[
  {
    "id": "65f9a1234567890abcdef123",
    "title": "Implement new feature",
    "description": "Implement the new authentication feature",
    "imageUrl": "https://example.com/image.png",
    "assignedUserId": "user123",
    "status": "ASSIGNED",
    "deadline": "2024-12-31T23:59:59",
    "createAt": "2024-11-10T10:30:00",
    "tags": ["backend", "security"]
  }
]
```

---

### 6. Update Task
Update an existing task's information.

**Request:**
```http
PUT /api/tasks/65f9a1234567890abcdef123
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Implement new feature (Updated)",
  "description": "Updated description",
  "status": "ASSIGNED",
  "deadline": "2024-12-25T23:59:59",
  "tags": ["backend", "security", "updated"]
}
```

**Response (200 OK):**
```json
{
  "id": "65f9a1234567890abcdef123",
  "title": "Implement new feature (Updated)",
  "description": "Updated description",
  "imageUrl": "https://example.com/image.png",
  "assignedUserId": "user123",
  "status": "ASSIGNED",
  "deadline": "2024-12-25T23:59:59",
  "createAt": "2024-11-10T10:30:00",
  "tags": ["backend", "security", "updated"]
}
```

---

### 7. Assign Task to User
Assign a task to a specific user.

**Request:**
```http
PUT /api/tasks/65f9a1234567890abcdef123/user/user456/assigned
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "id": "65f9a1234567890abcdef123",
  "title": "Implement new feature",
  "description": "Implement the new authentication feature",
  "imageUrl": "https://example.com/image.png",
  "assignedUserId": "user456",
  "status": "ASSIGNED",
  "deadline": "2024-12-31T23:59:59",
  "createAt": "2024-11-10T10:30:00",
  "tags": ["backend", "security"]
}
```

---

### 8. Complete Task
Mark a task as completed.

**Request:**
```http
PUT /api/tasks/65f9a1234567890abcdef123/complete
Authorization: Bearer <jwt_token>
```

**Response (200 OK):**
```json
{
  "id": "65f9a1234567890abcdef123",
  "title": "Implement new feature",
  "description": "Implement the new authentication feature",
  "imageUrl": "https://example.com/image.png",
  "assignedUserId": "user123",
  "status": "DONE",
  "deadline": "2024-12-31T23:59:59",
  "createAt": "2024-11-10T10:30:00",
  "tags": ["backend", "security"]
}
```

---

### 9. Delete Task
Delete a task by ID.

**Request:**
```http
DELETE /api/tasks/65f9a1234567890abcdef123
Authorization: Bearer <jwt_token>
```

**Response (204 NO CONTENT):**
```
(No body returned)
```

---

## Task Status Flow

```
PENDING → ASSIGNED → DONE
```

1. **PENDING**: Task created by admin, not assigned to anyone
2. **ASSIGNED**: Task assigned to a user
3. **DONE**: Task marked as completed

---

## Common Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Invalid request parameters",
  "status": 400,
  "error": "Bad Request"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "JWT required...",
  "status": 401,
  "error": "Unauthorized"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Only admin can create tasks",
  "status": 403,
  "error": "Forbidden"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Task not found with id: 65f9a1234567890abcdef123",
  "status": 404,
  "error": "Task Not Found"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-11-10T10:30:00",
  "message": "Internal server error",
  "status": 500,
  "error": "Internal Server Error"
}
```

---

## Testing with cURL

### Create Task (Admin)
```bash
curl -X POST http://localhost:8082/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Task",
    "description": "This is a test task",
    "deadline": "2024-12-31T23:59:59",
    "tags": ["test"]
  }'
```

### Get All Tasks
```bash
curl -X GET "http://localhost:8082/api/tasks?status=PENDING" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Task by ID
```bash
curl -X GET http://localhost:8082/api/tasks/TASK_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update Task
```bash
curl -X PUT http://localhost:8082/api/tasks/TASK_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Task Title",
    "status": "ASSIGNED"
  }'
```

### Complete Task
```bash
curl -X PUT http://localhost:8082/api/tasks/TASK_ID/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Delete Task
```bash
curl -X DELETE http://localhost:8082/api/tasks/TASK_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Notes

- All timestamps are in ISO 8601 format
- Task IDs are MongoDB ObjectIds (24-character hexadecimal strings)
- JWT tokens must be obtained from the User Service authentication endpoint
- Only admin users can create tasks
- Tasks can be in one of three states: PENDING, ASSIGNED, or DONE
- Sorting can be applied to either deadline or creation date, but not both simultaneously
