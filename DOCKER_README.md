# 🐳 Docker 部署文件说明

## 📁 文件清单

本次为 PandaCoder-Vault 项目生成了以下 Docker 相关文件：

### 核心文件

| 文件 | 位置 | 说明 |
|------|------|------|
| `Dockerfile` | `backend/Dockerfile` | 后端 Spring Boot 应用的 Docker 镜像构建文件 |
| `Dockerfile` | `frontend/Dockerfile` | 前端 React 应用的 Docker 镜像构建文件 |
| `nginx.conf` | `frontend/nginx.conf` | 前端 Nginx 服务器配置文件 |
| `docker-compose.yml` | 根目录 | Docker Compose 编排文件，用于一键启动所有服务 |

### 配置文件

| 文件 | 位置 | 说明 |
|------|------|------|
| `.dockerignore` | `backend/.dockerignore` | 后端 Docker 构建忽略文件 |
| `.dockerignore` | `frontend/.dockerignore` | 前端 Docker 构建忽略文件 |
| `.env.docker.example` | 根目录 | Docker 环境变量配置示例文件 |

### 启动脚本

| 文件 | 位置 | 说明 |
|------|------|------|
| `docker-start.sh` | 根目录 | Linux/Mac 一键启动脚本 |
| `docker-start.ps1` | 根目录 | Windows PowerShell 一键启动脚本 |

### 文档

| 文件 | 位置 | 说明 |
|------|------|------|
| `DOCKER_DEPLOYMENT.md` | 根目录 | 详细的 Docker 部署指南 |
| `DOCKER_README.md` | 根目录 | 本文件，Docker 文件说明 |

## 🚀 快速开始

### 方式 1: 使用一键启动脚本（推荐）

#### Linux/Mac

```bash
# 添加执行权限
chmod +x docker-start.sh

# 运行启动脚本
./docker-start.sh
```

#### Windows PowerShell

```powershell
# 运行启动脚本
.\docker-start.ps1
```

脚本会自动完成以下操作：
- ✅ 检查 Docker 和 Docker Compose 是否安装
- ✅ 创建并配置 `.env` 文件
- ✅ 生成安全的 JWT 密钥
- ✅ 构建 Docker 镜像
- ✅ 启动所有服务
- ✅ 等待服务就绪
- ✅ 显示访问地址

### 方式 2: 手动启动

```bash
# 1. 复制环境变量配置文件
cp .env.docker.example .env

# 2. 编辑 .env 文件，修改配置
vim .env

# 3. 启动所有服务
docker-compose up -d

# 4. 查看服务状态
docker-compose ps

# 5. 查看日志
docker-compose logs -f
```

## 📋 文件详细说明

### 1. backend/Dockerfile

**特性：**
- 多阶段构建，优化镜像大小
- 使用 Maven 3.9 + Eclipse Temurin JDK 21 构建
- 使用 Eclipse Temurin JRE 21 Alpine 运行
- 非 root 用户运行，提高安全性
- 配置健康检查
- JVM 参数优化
- 时区设置为 Asia/Shanghai

**构建命令：**
```bash
cd backend
docker build -t pandacoder-vault-backend:1.0.0 .
```

**运行命令：**
```bash
docker run -d \
  --name pandacoder-backend \
  -p 8080:8080 \
  -e MONGODB_HOST=your-mongodb-host \
  -e MONGODB_USERNAME=your-username \
  -e MONGODB_PASSWORD=your-password \
  -e JWT_SECRET=your-jwt-secret \
  pandacoder-vault-backend:1.0.0
```

### 2. frontend/Dockerfile

**特性：**
- 多阶段构建，优化镜像大小
- 使用 Node.js 18 Alpine 构建
- 使用 Nginx Alpine 提供静态文件服务
- Gzip 压缩
- 静态资源缓存
- React Router 支持
- API 代理配置
- 健康检查

**构建命令：**
```bash
cd frontend
docker build -t pandacoder-vault-frontend:1.0.0 .
```

**运行命令：**
```bash
docker run -d \
  --name pandacoder-frontend \
  -p 80:80 \
  pandacoder-vault-frontend:1.0.0
```

### 3. frontend/nginx.conf

**配置特性：**
- Gzip 压缩配置
- 静态资源缓存（1年）
- API 代理到后端服务
- React Router 支持（所有路由返回 index.html）
- 安全头部配置
- 超时设置

**主要配置：**
```nginx
# API 代理
location /api/ {
    proxy_pass http://backend:8080/api/;
}

# React Router 支持
location / {
    try_files $uri $uri/ /index.html;
}
```

### 4. docker-compose.yml

**服务列表：**
- `backend`: Spring Boot 后端服务
- `frontend`: React + Nginx 前端服务
- `mongodb`: MongoDB 数据库（可选）
- `mysql`: MySQL 数据库（可选）

**网络配置：**
- 所有服务在同一个 `pandacoder-network` 网络中
- 服务间可以通过服务名互相访问

**数据持久化：**
- `mongodb-data`: MongoDB 数据卷
- `mongodb-config`: MongoDB 配置卷
- `mysql-data`: MySQL 数据卷

**端口映射：**
- 前端: `80:80`
- 后端: `8080:8080`
- MongoDB: `27017:27017`
- MySQL: `3306:3306`

### 5. .env.docker.example

**环境变量说明：**

```properties
# MySQL 配置
MYSQL_HOST=mysql              # MySQL 主机地址
MYSQL_PORT=3306               # MySQL 端口
MYSQL_DATABASE=PandaCoder     # 数据库名
MYSQL_USERNAME=root           # 用户名
MYSQL_PASSWORD=your-password  # 密码（必须修改）

# MongoDB 配置
MONGODB_HOST=mongodb          # MongoDB 主机地址
MONGODB_PORT=27017            # MongoDB 端口
MONGODB_DATABASE=PandaCoder   # 数据库名
MONGODB_USERNAME=admin        # 用户名
MONGODB_PASSWORD=your-password # 密码（必须修改）
MONGODB_AUTH_DATABASE=admin   # 认证数据库

# JWT 配置
JWT_SECRET=your-secret-key    # JWT 密钥（必须修改）
JWT_EXPIRATION=86400000       # Token 有效期（毫秒）
```

## 🎯 使用场景

### 场景 1: 完整部署（包括数据库）

适用于开发、测试环境，或者没有外部数据库的情况。

```bash
# 启动所有服务（包括 MongoDB 和 MySQL）
docker-compose up -d
```

### 场景 2: 仅部署应用（使用外部数据库）

适用于生产环境，已有独立的数据库服务。

```bash
# 1. 修改 .env 文件，配置外部数据库地址
MONGODB_HOST=your-external-mongodb-host
MYSQL_HOST=your-external-mysql-host

# 2. 仅启动应用服务
docker-compose up -d backend frontend
```

### 场景 3: 单独构建和部署

适用于 Kubernetes 或其他容器编排平台。

```bash
# 构建镜像
docker build -t your-registry/pandacoder-backend:1.0.0 ./backend
docker build -t your-registry/pandacoder-frontend:1.0.0 ./frontend

# 推送到镜像仓库
docker push your-registry/pandacoder-backend:1.0.0
docker push your-registry/pandacoder-frontend:1.0.0
```

## 📊 镜像大小优化

通过多阶段构建，镜像大小得到了显著优化：

| 镜像 | 预估大小 | 说明 |
|------|---------|------|
| 后端镜像 | ~300MB | 使用 JRE Alpine 基础镜像 |
| 前端镜像 | ~25MB | 使用 Nginx Alpine 基础镜像 |

## 🔒 安全建议

1. **修改默认密码**: 生产环境必须修改 `.env` 中的所有默认密码
2. **使用强 JWT 密钥**: 使用至少 64 字节的随机字符串
3. **限制端口暴露**: 生产环境仅暴露必要的端口
4. **使用 HTTPS**: 配置 SSL/TLS 证书
5. **定期更新**: 及时更新基础镜像以修复安全漏洞

## 📚 更多信息

详细的部署指南、故障排查和性能优化，请查看：
- [DOCKER_DEPLOYMENT.md](./DOCKER_DEPLOYMENT.md) - 完整的 Docker 部署指南

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**快速帮助**: 遇到问题请查看 [DOCKER_DEPLOYMENT.md](./DOCKER_DEPLOYMENT.md) 或提交 Issue

