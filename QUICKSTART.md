# 🚀 Quick Start Guide

## Prerequisites

Make sure you have these installed:
- ✅ Docker & Docker Compose
- ✅ Java 17 or higher
- ✅ Node.js & npm (for frontend)

---

## 🎯 Option 1: Start Everything at Once (Recommended)

### Start All Services (Backend + Frontend)
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
./start-all.sh
```

### Stop All Services
```bash
./stop-all.sh
```

**That's it!** After running `./start-all.sh`:
- Backend services will be ready in ~30 seconds
- Frontend will be available at http://localhost:3000 in ~45 seconds

---

## 🎯 Option 2: Start Services Separately

### Backend Services Only

**Start:**
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
./run-system.sh
```

**Stop:**
```bash
./stop-system.sh
```

### Frontend Only

**Start:**
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices/task-management-ui
npm install    # First time only
npm start
```

**Stop:** Press `Ctrl+C` in the terminal

---

## 🎯 Option 3: Manual Docker Commands

If the scripts don't work, you can use Docker commands directly:

### Start Backend
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices

# Build all services first
./build-all.sh

# Start with Docker Compose
docker-compose up -d

# Check status
docker-compose ps
```

### Stop Backend
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
docker-compose down
```

---

## 📊 Access the Application

Once everything is running:

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:3000 | React web interface |
| **API Gateway** | http://localhost:8090 | Main API endpoint |
| **Eureka Dashboard** | http://localhost:8085 | Service discovery |
| **Zipkin** | http://localhost:9411 | Distributed tracing |
| **User Service** | http://localhost:8081 | User management |
| **Task Service** | http://localhost:8082 | Task management |
| **Submission Service** | http://localhost:8083 | Submission management |

---

## 🔍 Troubleshooting

### Check if services are running
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
docker-compose ps
```

All services should show "Up (healthy)".

### View logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f api-gateway
docker-compose logs -f user-service
```

### Services not starting?
```bash
# Stop everything
docker-compose down

# Remove old containers and volumes
docker-compose down -v

# Rebuild and start fresh
./build-all.sh
docker-compose up -d
```

### Frontend not loading?
```bash
cd task-management-ui

# Check if .env file exists
cat .env

# Should show: REACT_APP_API_BASE_URL=http://localhost:8090

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
npm start
```

### Port already in use?
```bash
# Check what's using the port (e.g., 3000)
lsof -i :3000

# Kill the process
kill -9 <PID>
```

---

## 🏗️ Architecture

```
┌─────────────────┐
│   React UI      │ ← You interact here (localhost:3000)
│  (Frontend)     │
└────────┬────────┘
         │ HTTP Requests
         ↓
┌─────────────────┐
│  API Gateway    │ ← All requests go through here (localhost:8090)
│  (Port 8090)    │
└────────┬────────┘
         │ Service Discovery
         ↓
┌─────────────────┐
│ Eureka Server   │ ← Manages all microservices (localhost:8085)
│  (Port 8085)    │
└────────┬────────┘
         │
    ┌────┴────┬────────────┐
    ↓         ↓            ↓
┌─────────┐ ┌──────────┐ ┌─────────────┐
│  User   │ │   Task   │ │ Submission  │
│ Service │ │ Service  │ │  Service    │
│ (8081)  │ │ (8082)   │ │  (8083)     │
└────┬────┘ └────┬─────┘ └──────┬──────┘
     │           │               │
     └───────────┴───────────────┘
                 │
           ┌─────┴──────┐
           │  MongoDB   │ ← Database (localhost:27017)
           └────────────┘
```

---

## 📝 Development Workflow

### Starting development for the day
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
./start-all.sh
```

### Making changes to backend code
1. Make your changes in the respective service folder
2. Rebuild that specific service:
   ```bash
   cd TaskUserService  # or TaskService, etc.
   ./gradlew build
   ```
3. Restart the service:
   ```bash
   docker-compose restart user-service
   ```

### Making changes to frontend code
- Just save the file - React hot reloads automatically! ✨

### Ending development for the day
```bash
cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
./stop-all.sh
```

---

## 🎓 First Time Setup

If this is your first time running the project:

1. **Clone the repository** (if not already done)
2. **Navigate to the project:**
   ```bash
   cd /home/garvitpathak27/cloud_computing/Task-Management-System-using-microservices
   ```

3. **Install frontend dependencies:**
   ```bash
   cd task-management-ui
   npm install
   cd ..
   ```

4. **Start everything:**
   ```bash
   ./start-all.sh
   ```

5. **Wait ~1 minute** for all services to start

6. **Open your browser:** http://localhost:3000

---

## ✅ Current Status

**All services are currently running!** 🎉

You can verify by running:
```bash
docker-compose ps
```

To stop them:
```bash
./stop-all.sh
```

To restart them:
```bash
./start-all.sh
```

---

## 💡 Tips

- **First startup takes longer** (~2-3 minutes) as Docker downloads images
- **Subsequent startups are faster** (~30 seconds)
- **Frontend compilation** takes ~10-15 seconds
- **Check Eureka dashboard** to see all registered services
- **Use Zipkin** to trace requests across microservices
- **MongoDB data persists** between restarts (stored in Docker volume)

---

## 🆘 Need Help?

1. Check the logs: `docker-compose logs -f`
2. Verify all services are healthy: `docker-compose ps`
3. Make sure ports are not in use: `lsof -i :3000,8081,8082,8083,8090`
4. See detailed troubleshooting: [FRONTEND_INTEGRATION_ISSUES.md](./FRONTEND_INTEGRATION_ISSUES.md)

---

**Happy Coding! 🚀**
