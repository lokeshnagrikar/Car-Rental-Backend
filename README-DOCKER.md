# Car Rental API - Docker Setup Guide

## 🚀 Quick Start
1. Install [Docker](https://www.docker.com/get-started)
2. Clone the repository
3. Create a [.env](cci:7://file:///D:/My-Car-Project/Car-Rental-Backend/.env:0:0-0:0) file (see Configuration section)
4. Run: `docker-compose up --build`
5. Access: http://localhost:8081/api

## 📋 Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- 4GB+ free RAM
- Git

## 🏗️ Project Structure
Car-Rental-Backend/ ├── src/ # Source code ├── docker-compose.yml # Multi-container setup ├── Dockerfile # Backend service ├── init.sql # Database schema & data └── .env # Environment variables


## 🛠️ Services

### 1. Backend API (Spring Boot)
- **Port**: 8081 (configurable via [.env](cci:7://file:///D:/My-Car-Project/Car-Rental-Backend/.env:0:0-0:0))
- **Context Path**: `/api`
- **Health Check**: `/api/actuator/health`
- **Swagger UI**: `/api/swagger-ui.html`
- **Logs**: [./logs](cci:7://file:///D:/My-Car-Project/Car-Rental-Backend/logs:0:0-0:0) directory mounted to container

### 2. MySQL Database
- **Version**: 8.2.0
- **Port**: 3306 (host) → 3306 (container)
- **Database**: `car_db` (configurable)
- **Root User**: `root` (password from [.env](cci:7://file:///D:/My-Car-Project/Car-Rental-Backend/.env:0:0-0:0))
- **App User**: Configurable in [.env](cci:7://file:///D:/My-Car-Project/Car-Rental-Backend/.env:0:0-0:0)
- **Data Persistence**: Docker volume `car_rental_mysql_data`

## 🔧 Configuration

### Environment Variables ([.env](cci:7://file:///D:/My-Car-Project/Car-Rental-Backend/.env:0:0-0:0) file)
```env
# Application
SERVER_PORT=8081
SPRING_PROFILES_ACTIVE=dev
TZ=Asia/Kolkata

# Database
DB_NAME=car_db
DB_USER=root
DB_PASSWORD=Root123
DB_ROOT_PASSWORD=Root123
DB_PORT=3306

# File Uploads
SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE=10MB
SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE=10MB
🚦 Commands
Development
bash
# Start all services
docker-compose up --build

# Start in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
Database
bash
# Connect as root
docker exec -it mysql-db mysql -uroot -p${DB_PASSWORD}

# View database logs
docker-compose logs mysql-db

# Backup database
docker exec mysql-db /usr/bin/mysqldump -u root --password=${DB_ROOT_PASSWORD} ${DB_NAME} > backup.sql
Maintenance
bash
# Rebuild everything
docker-compose down -v
docker-compose up --build

# Check running containers
docker ps

# View resource usage
docker stats

# Clean up unused resources
docker system prune
🔍 Troubleshooting
Port Conflicts
Update ports in 
docker-compose.yml
:

yaml
services:
  backend:
    ports:
      - "8082:8081"  
  mysql:
    ports:
      - "3306:3306"  
Database Issues
bash
# Check logs
docker-compose logs mysql-db

# Reset database (WARNING: Deletes all data)
docker-compose down -v
docker-compose up -d
Common Errors
Connection refused: Wait for MySQL to initialize (30-60s)
Port in use: Stop other services using ports 8081/3306
Build failures: Check Docker logs for specific errors
🚀 Deployment
Production Checklist
Change all default passwords in 
.env
Set SPRING_PROFILES_ACTIVE=prod
Enable HTTPS
Configure proper logging
Set up database backups
Configure monitoring
Docker Compose Override
Create docker-compose.override.yml:

yaml
version: '3.8'
services:
  backend:
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: always
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
  mysql:
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 1G
    restart: always
📚 Documentation
Spring Boot Documentation
Docker Documentation
MySQL Docker Image
📝 License
[Your License Here]

