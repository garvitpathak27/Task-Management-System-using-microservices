# Frontend Integration Issues & Solutions

## Issues Found and Fixed

### 1. **API Gateway Configuration Issue** ✅ FIXED
**Problem:** The API Gateway was constantly restarting due to an invalid Spring Boot configuration.

**Root Cause:** In `APIGateWay/src/main/resources/application-docker.yml`, the property `spring.profiles.active: docker` was set inside a profile-specific configuration file, which is not allowed in Spring Boot.

**Fix Applied:**
```yaml
# REMOVED this line from application-docker.yml:
spring:
  profiles:
    active: docker  # ❌ Invalid in profile-specific files
```

**Status:** ✅ API Gateway is now healthy and running on port 8090

---

## Remaining Issues to Fix

### 2. **Missing Frontend .env File** ⚠️ CRITICAL
**Problem:** The frontend doesn't have a `.env` file to configure the API endpoint.

**Impact:** Frontend will use the default `http://localhost:8090` which won't work in Docker environment.

**Solution Required:**
Create `/home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices/task-management-ui/.env`:
```bash
REACT_APP_API_BASE_URL=http://localhost:8090
```

**For Docker deployment (if running UI in container):**
```bash
REACT_APP_API_BASE_URL=http://api-gateway:8090
```

---

### 3. **Frontend Not Running** ⚠️ CRITICAL
**Problem:** The frontend service is not included in `docker-compose.yml` and is not running.

**Impact:** Users cannot access the web interface.

**Solutions:**

#### Option A: Run Frontend Locally (Recommended for Development)
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices/task-management-ui
npm install
npm start
```

#### Option B: Add Frontend to Docker Compose
1. Create `task-management-ui/Dockerfile`:
```dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ENV REACT_APP_API_BASE_URL=http://localhost:8090
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
```

2. Add to `docker-compose.yml`:
```yaml
  frontend:
    build:
      context: ./task-management-ui
      dockerfile: Dockerfile
    container_name: task-management-ui
    restart: unless-stopped
    ports:
      - "3000:80"
    environment:
      REACT_APP_API_BASE_URL: http://localhost:8090
    networks:
      - task-management-network
```

---

### 4. **CORS Configuration Review** ℹ️ INFO
**Current Status:** CORS is properly configured in multiple places:

1. **API Gateway** (`application.yml`):
   - Allows origins: `http://localhost:3000`, `http://localhost:8080`
   - Allows methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
   - Allows credentials: true

2. **User Service** (`SecurityConfig.java`):
   - Configurable via environment variable: `TASK_ALLOWED_ORIGINS`
   - Docker config allows: `http://task-management-ui:3000,http://localhost:3000`

**Note:** Make sure your frontend runs on `http://localhost:3000` to match CORS configuration.

---

## How to Test the Integration

### 1. Start Backend Services (Already Running ✅)
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
docker-compose -f docker-compose.yml ps
```

### 2. Start Frontend
```bash
cd task-management-ui
echo "REACT_APP_API_BASE_URL=http://localhost:8090" > .env
npm install
npm start
```

### 3. Verify Services
- **Frontend:** http://localhost:3000
- **API Gateway:** http://localhost:8090
- **Eureka Dashboard:** http://localhost:8085
- **Zipkin:** http://localhost:9411

### 4. Test API Endpoints
```bash
# Health check
curl http://localhost:8090/actuator/health

# Auth endpoint (should return 401 or error without credentials)
curl http://localhost:8090/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123"}'
```

---

## Architecture Overview

```
┌─────────────────┐
│   React UI      │ (Port 3000)
│  (Frontend)     │
└────────┬────────┘
         │ HTTP Requests
         ↓
┌─────────────────┐
│  API Gateway    │ (Port 8090) ✅ Fixed
│  Spring Cloud   │
└────────┬────────┘
         │ Load Balancing via Eureka
         ↓
┌────────────────────────────────────┐
│         Eureka Server              │ (Port 8085)
│      Service Discovery             │
└────────────────┬───────────────────┘
                 │
    ┌────────────┼────────────┐
    ↓            ↓            ↓
┌─────────┐ ┌──────────┐ ┌─────────────┐
│  User   │ │   Task   │ │ Submission  │
│ Service │ │ Service  │ │  Service    │
│ (8081)  │ │ (8082)   │ │  (8083)     │
└────┬────┘ └────┬─────┘ └──────┬──────┘
     │           │               │
     └───────────┴───────────────┘
                 │
           ┌─────┴──────┐
           │  MongoDB   │ (Port 27017)
           └────────────┘
```

---

## Next Steps

1. ✅ **Fixed:** API Gateway configuration issue
2. ⚠️ **TODO:** Create `.env` file in `task-management-ui/`
3. ⚠️ **TODO:** Start the frontend application
4. ℹ️ **Optional:** Add frontend to Docker Compose for production deployment
5. 🧪 **Test:** Complete end-to-end testing of authentication and API calls

---

## Quick Start Frontend

Run these commands to start the frontend:

```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices/task-management-ui
echo "REACT_APP_API_BASE_URL=http://localhost:8090" > .env
npm install
npm start
```

The frontend will be available at **http://localhost:3000**
