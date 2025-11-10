# 🐳 Docker 快速启动指南

## 🎯 一分钟快速启动

### Windows 用户

```powershell
# 运行启动脚本
.\docker-start.ps1
```

### Linux/Mac 用户

```bash
# 运行启动脚本
./docker-start.sh
```

脚本会自动完成所有配置和启动工作！

## 📋 前置要求

- ✅ Docker Desktop 已安装并运行
- ✅ Docker Compose 已安装（Docker Desktop 自带）

### 检查 Docker 是否安装

```bash
# 检查 Docker
docker --version

# 检查 Docker Compose
docker-compose --version
```

如果未安装，请访问：https://docs.docker.com/get-docker/

## 🚀 手动启动步骤

如果不想使用启动脚本，可以手动执行以下步骤：

### 步骤 1: 配置环境变量

```bash
# 复制环境变量配置文件
cp .env.docker.example .env
```

### 步骤 2: 编辑配置文件

编辑 `.env` 文件，修改以下配置：

```properties
# 修改 MongoDB 密码
MONGODB_PASSWORD=your-strong-password

# 修改 MySQL 密码
MYSQL_PASSWORD=your-strong-password

# 修改 JWT 密钥（重要！）
JWT_SECRET=your-very-long-random-secret-key
```

**生成安全的 JWT 密钥：**

```bash
# Linux/Mac
openssl rand -base64 64

# Windows PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

### 步骤 3: 启动服务

```bash
# 启动所有服务（包括数据库）
docker-compose up -d
```

### 步骤 4: 等待服务就绪

```bash
# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

等待后端服务启动完成（约 30-60 秒）

### 步骤 5: 访问应用

- **前端**: http://localhost
- **后端 API**: http://localhost:8080/api
- **健康检查**: http://localhost:8080/api/auth/test

**默认账号**: admin / admin123

## 🎨 部署选项

### 选项 1: 完整部署（推荐用于开发/测试）

包括前端、后端、MongoDB、MySQL 所有服务。

```bash
docker-compose up -d
```

### 选项 2: 仅部署应用（使用外部数据库）

适用于生产环境，已有独立的数据库服务。

```bash
# 1. 修改 .env 文件，配置外部数据库地址
MONGODB_HOST=your-external-mongodb-host
MYSQL_HOST=your-external-mysql-host

# 2. 仅启动应用服务
docker-compose up -d backend frontend
```

### 选项 3: 单独构建镜像

适用于 Kubernetes 或其他容器编排平台。

```bash
# 构建后端镜像
cd backend
docker build -t pandacoder-vault-backend:1.0.0 .

# 构建前端镜像
cd frontend
docker build -t pandacoder-vault-frontend:1.0.0 .
```

## 🔧 常用命令

```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 查看服务状态
docker-compose ps

# 查看日志（所有服务）
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend

# 进入容器
docker-compose exec backend sh
docker-compose exec frontend sh

# 重新构建镜像
docker-compose build --no-cache

# 停止并删除所有容器和网络（保留数据）
docker-compose down

# 停止并删除所有容器、网络和数据卷（谨慎！）
docker-compose down -v
```

## 🐛 常见问题

### 1. 端口被占用

**错误**: `Bind for 0.0.0.0:80 failed: port is already allocated`

**解决方案**:

```bash
# 查看端口占用（Windows）
netstat -ano | findstr :80

# 修改 docker-compose.yml 中的端口映射
# 例如：将 "80:80" 改为 "8000:80"
```

### 2. 后端无法连接数据库

**解决方案**:

```bash
# 检查数据库容器是否运行
docker-compose ps

# 查看数据库日志
docker-compose logs mongodb
docker-compose logs mysql

# 重启服务
docker-compose restart
```

### 3. 前端无法访问后端 API

**解决方案**:

```bash
# 检查后端健康状态
curl http://localhost:8080/api/auth/test

# 查看后端日志
docker-compose logs -f backend

# 检查容器间网络
docker-compose exec frontend wget -O- http://backend:8080/api/auth/test
```

### 4. 容器频繁重启

**解决方案**:

```bash
# 查看容器日志
docker-compose logs --tail=100 backend

# 检查资源使用情况
docker stats

# 重新构建并启动
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

## 📚 更多文档

- [DOCKER_DEPLOYMENT.md](./DOCKER_DEPLOYMENT.md) - 详细的部署指南
- [DOCKER_README.md](./DOCKER_README.md) - Docker 文件说明
- [DOCKER_FILES_SUMMARY.md](./DOCKER_FILES_SUMMARY.md) - 生成文件总结

## 🎉 启动成功后

访问 http://localhost，使用默认账号登录：
- **用户名**: admin
- **密码**: admin123

⚠️ **重要**: 首次登录后请立即修改密码！

---

**快速帮助**: 遇到问题请查看 [常见问题](#常见问题) 或查阅详细文档

