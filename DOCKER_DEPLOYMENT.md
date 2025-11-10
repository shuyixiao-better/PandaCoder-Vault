# 🐳 PandaCoder-Vault Docker 部署指南

## 📋 目录

- [快速开始](#快速开始)
- [文件说明](#文件说明)
- [部署方式](#部署方式)
- [配置说明](#配置说明)
- [常用命令](#常用命令)
- [故障排查](#故障排查)

## 🚀 快速开始

### 前置要求

- Docker 20.10+
- Docker Compose 2.0+

### 一键部署（推荐）

```bash
# 1. 复制环境变量配置文件
cp .env.docker.example .env

# 2. 编辑 .env 文件，修改数据库密码和 JWT 密钥
vim .env

# 3. 启动所有服务（包括数据库）
docker-compose up -d

# 4. 查看服务状态
docker-compose ps

# 5. 查看日志
docker-compose logs -f
```

访问应用：
- **前端**: http://localhost
- **后端 API**: http://localhost:8080/api
- **健康检查**: http://localhost:8080/api/auth/test

## 📁 文件说明

### Docker 相关文件

```
PandaCoder-Vault/
├── docker-compose.yml          # Docker Compose 编排文件
├── .env.docker.example         # 环境变量配置示例
├── backend/
│   ├── Dockerfile             # 后端 Dockerfile
│   └── .dockerignore          # 后端 Docker 忽略文件
└── frontend/
    ├── Dockerfile             # 前端 Dockerfile
    ├── nginx.conf             # Nginx 配置文件
    └── .dockerignore          # 前端 Docker 忽略文件
```

### Dockerfile 特性

#### 后端 Dockerfile
- ✅ 多阶段构建，优化镜像大小
- ✅ 使用 Eclipse Temurin JRE 21
- ✅ 非 root 用户运行
- ✅ 健康检查配置
- ✅ JVM 参数优化
- ✅ 时区设置（Asia/Shanghai）

#### 前端 Dockerfile
- ✅ 多阶段构建，优化镜像大小
- ✅ 使用 Nginx Alpine 镜像
- ✅ Gzip 压缩
- ✅ 静态资源缓存
- ✅ React Router 支持
- ✅ API 代理配置

## 🎯 部署方式

### 方式 1: 使用 Docker Compose（推荐）

适用于完整部署，包括数据库。

```bash
# 启动所有服务
docker-compose up -d

# 仅启动应用（使用外部数据库）
docker-compose up -d backend frontend
```

### 方式 2: 单独构建镜像

适用于自定义部署或 Kubernetes。

#### 构建后端镜像

```bash
cd backend
docker build -t pandacoder-vault-backend:1.0.0 .
```

#### 构建前端镜像

```bash
cd frontend
docker build -t pandacoder-vault-frontend:1.0.0 .
```

#### 运行容器

```bash
# 运行后端
docker run -d \
  --name pandacoder-backend \
  -p 8080:8080 \
  -e MONGODB_HOST=your-mongodb-host \
  -e MONGODB_USERNAME=your-username \
  -e MONGODB_PASSWORD=your-password \
  -e JWT_SECRET=your-jwt-secret \
  pandacoder-vault-backend:1.0.0

# 运行前端
docker run -d \
  --name pandacoder-frontend \
  -p 80:80 \
  pandacoder-vault-frontend:1.0.0
```

### 方式 3: 使用外部数据库

如果已有 MongoDB 和 MySQL 数据库，可以只部署应用服务。

```bash
# 1. 修改 .env 文件，配置外部数据库地址
MONGODB_HOST=your-external-mongodb-host
MYSQL_HOST=your-external-mysql-host

# 2. 仅启动应用服务
docker-compose up -d backend frontend
```

## ⚙️ 配置说明

### 环境变量

在 `.env` 文件中配置以下变量：

```properties
# MySQL 配置
MYSQL_HOST=mysql                    # MySQL 主机地址
MYSQL_PORT=3306                     # MySQL 端口
MYSQL_DATABASE=PandaCoder           # 数据库名
MYSQL_USERNAME=root                 # 用户名
MYSQL_PASSWORD=your-password        # 密码

# MongoDB 配置
MONGODB_HOST=mongodb                # MongoDB 主机地址
MONGODB_PORT=27017                  # MongoDB 端口
MONGODB_DATABASE=PandaCoder         # 数据库名
MONGODB_USERNAME=admin              # 用户名
MONGODB_PASSWORD=your-password      # 密码
MONGODB_AUTH_DATABASE=admin         # 认证数据库

# JWT 配置
JWT_SECRET=your-secret-key          # JWT 密钥（生产环境必须修改）
JWT_EXPIRATION=86400000             # Token 有效期（毫秒）
```

### 生成安全的 JWT 密钥

```bash
# 使用 OpenSSL 生成随机密钥
openssl rand -base64 64
```

### 端口配置

| 服务 | 容器端口 | 主机端口 | 说明 |
|------|---------|---------|------|
| 前端 | 80 | 80 | Nginx Web 服务器 |
| 后端 | 8080 | 8080 | Spring Boot API |
| MongoDB | 27017 | 27017 | MongoDB 数据库 |
| MySQL | 3306 | 3306 | MySQL 数据库 |

## 🔧 常用命令

### 服务管理

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose down

# 重启服务
docker-compose restart

# 停止并删除所有容器、网络、数据卷
docker-compose down -v

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
```

### 镜像管理

```bash
# 构建镜像
docker-compose build

# 重新构建镜像（不使用缓存）
docker-compose build --no-cache

# 拉取镜像
docker-compose pull

# 查看镜像
docker images | grep pandacoder
```

### 容器管理

```bash
# 进入后端容器
docker-compose exec backend sh

# 进入前端容器
docker-compose exec frontend sh

# 进入 MongoDB 容器
docker-compose exec mongodb mongosh

# 进入 MySQL 容器
docker-compose exec mysql mysql -uroot -p
```

### 数据管理

```bash
# 备份 MongoDB 数据
docker-compose exec mongodb mongodump --out /data/backup

# 备份 MySQL 数据
docker-compose exec mysql mysqldump -uroot -p PandaCoder > backup.sql

# 查看数据卷
docker volume ls | grep pandacoder

# 删除数据卷（谨慎操作）
docker volume rm pandacoder-vault_mongodb-data
docker volume rm pandacoder-vault_mysql-data
```

## 🐛 故障排查

### 1. 后端无法连接数据库

**问题**: 后端启动失败，提示无法连接 MongoDB 或 MySQL

**解决方案**:

```bash
# 检查数据库容器是否运行
docker-compose ps

# 查看数据库日志
docker-compose logs mongodb
docker-compose logs mysql

# 检查网络连接
docker-compose exec backend ping mongodb
docker-compose exec backend ping mysql

# 验证环境变量
docker-compose exec backend env | grep MONGODB
docker-compose exec backend env | grep MYSQL
```

### 2. 前端无法访问后端 API

**问题**: 前端页面加载正常，但 API 请求失败

**解决方案**:

```bash
# 检查后端健康状态
curl http://localhost:8080/api/auth/test

# 查看后端日志
docker-compose logs -f backend

# 检查 Nginx 配置
docker-compose exec frontend cat /etc/nginx/conf.d/default.conf

# 测试容器间网络
docker-compose exec frontend wget -O- http://backend:8080/api/auth/test
```

### 3. 容器启动失败

**问题**: 容器无法启动或频繁重启

**解决方案**:

```bash
# 查看容器详细信息
docker-compose ps -a

# 查看容器日志
docker-compose logs --tail=100 backend
docker-compose logs --tail=100 frontend

# 检查资源使用情况
docker stats

# 重新构建并启动
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### 4. 端口冲突

**问题**: 端口已被占用

**解决方案**:

```bash
# 查看端口占用情况（Linux/Mac）
lsof -i :80
lsof -i :8080
lsof -i :27017
lsof -i :3306

# 查看端口占用情况（Windows PowerShell）
netstat -ano | findstr :80
netstat -ano | findstr :8080

# 修改 docker-compose.yml 中的端口映射
# 例如：将 "80:80" 改为 "8000:80"
```

### 5. 数据持久化问题

**问题**: 容器重启后数据丢失

**解决方案**:

```bash
# 检查数据卷是否正确挂载
docker volume inspect pandacoder-vault_mongodb-data
docker volume inspect pandacoder-vault_mysql-data

# 确保使用 docker-compose down 而不是 docker-compose down -v
# -v 参数会删除数据卷
```

## 📊 性能优化

### JVM 参数调优

在 `docker-compose.yml` 中调整后端 JVM 参数：

```yaml
environment:
  JAVA_OPTS: "-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### Nginx 优化

编辑 `frontend/nginx.conf`，添加更多优化配置：

```nginx
# 增加 worker 进程
worker_processes auto;

# 调整连接数
events {
    worker_connections 1024;
}
```

### 资源限制

在 `docker-compose.yml` 中添加资源限制：

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

## 🔒 安全建议

1. **修改默认密码**: 生产环境必须修改所有默认密码
2. **使用强 JWT 密钥**: 使用至少 64 字节的随机字符串
3. **启用 HTTPS**: 在生产环境使用 SSL/TLS 证书
4. **限制端口暴露**: 仅暴露必要的端口
5. **定期更新镜像**: 及时更新基础镜像以修复安全漏洞
6. **使用 secrets**: 敏感信息使用 Docker secrets 管理

## 🚀 生产环境部署

### 使用 HTTPS

1. 获取 SSL 证书（Let's Encrypt 或其他 CA）
2. 修改 `frontend/nginx.conf` 添加 SSL 配置
3. 在 `docker-compose.yml` 中映射证书文件

### 使用反向代理

推荐在前面加一层 Nginx 或 Traefik 作为反向代理：

```nginx
# 外部 Nginx 配置示例
upstream frontend {
    server localhost:80;
}

upstream backend {
    server localhost:8080;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    location / {
        proxy_pass http://frontend;
    }

    location /api/ {
        proxy_pass http://backend;
    }
}
```

## 📝 更新日志

### v1.0.0 (2025-11-10)
- ✅ 初始版本
- ✅ 后端 Dockerfile（多阶段构建）
- ✅ 前端 Dockerfile（Nginx + React）
- ✅ Docker Compose 编排
- ✅ 健康检查配置
- ✅ 数据持久化支持

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

MIT License

---

**快速帮助**: 遇到问题请查看 [故障排查](#故障排查) 章节或提交 Issue

