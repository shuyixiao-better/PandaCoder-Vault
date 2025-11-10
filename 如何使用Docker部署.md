# 🐳 如何使用 Docker 部署 PandaCoder-Vault

## 📋 前置准备

### 1. 安装 Docker Desktop

#### Windows 用户
1. 访问 https://docs.docker.com/desktop/install/windows-install/
2. 下载并安装 Docker Desktop for Windows
3. 启动 Docker Desktop

#### Mac 用户
1. 访问 https://docs.docker.com/desktop/install/mac-install/
2. 下载并安装 Docker Desktop for Mac
3. 启动 Docker Desktop

#### Linux 用户
1. 访问 https://docs.docker.com/engine/install/
2. 根据您的 Linux 发行版选择安装方式
3. 安装 Docker Engine 和 Docker Compose

### 2. 验证安装

打开终端（Windows 用户使用 PowerShell），运行以下命令：

```bash
# 检查 Docker 版本
docker --version

# 检查 Docker Compose 版本
docker-compose --version

# 检查 Docker 是否运行
docker ps
```

如果以上命令都能正常执行，说明 Docker 已安装成功！

---

## 🚀 方式一：一键启动（推荐）

### Windows 用户

1. 打开 PowerShell
2. 进入项目目录
3. 运行启动脚本

```powershell
cd E:\Project\GitHub\PandaCoder-Vault
.\docker-start.ps1
```

### Linux/Mac 用户

1. 打开终端
2. 进入项目目录
3. 运行启动脚本

```bash
cd /path/to/PandaCoder-Vault
./docker-start.sh
```

### 脚本会自动完成以下操作：

- ✅ 检查 Docker 和 Docker Compose 是否安装
- ✅ 创建 `.env` 配置文件（如果不存在）
- ✅ 生成安全的 JWT 密钥
- ✅ 构建 Docker 镜像
- ✅ 启动所有服务
- ✅ 等待服务就绪
- ✅ 显示访问地址

---

## 🔧 方式二：手动启动

### 步骤 1: 创建配置文件

```bash
# 复制环境变量配置文件
cp .env.docker.example .env
```

### 步骤 2: 编辑配置文件

使用文本编辑器打开 `.env` 文件，修改以下配置：

```properties
# MongoDB 密码（必须修改）
MONGODB_PASSWORD=your-strong-password-here

# MySQL 密码（必须修改）
MYSQL_PASSWORD=your-strong-password-here

# JWT 密钥（必须修改，建议使用下面的命令生成）
JWT_SECRET=your-very-long-random-secret-key
```

#### 生成安全的 JWT 密钥

**Windows PowerShell:**
```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

**Linux/Mac:**
```bash
openssl rand -base64 64
```

将生成的密钥复制到 `.env` 文件的 `JWT_SECRET=` 后面。

### 步骤 3: 启动服务

```bash
# 启动所有服务（包括数据库）
docker-compose up -d
```

### 步骤 4: 查看服务状态

```bash
# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

等待后端服务启动完成（约 30-60 秒），看到以下日志表示启动成功：

```
backend  | 🐼 PandaCoder-Vault Backend Started Successfully! 🐼
```

---

## 📝 访问应用

启动成功后，打开浏览器访问：

- **前端应用**: http://localhost
- **后端 API**: http://localhost:8080/api
- **健康检查**: http://localhost:8080/api/auth/test

### 默认账号

- **用户名**: admin
- **密码**: admin123

⚠️ **重要**: 首次登录后请立即修改密码！

---

## 🎨 部署选项

### 选项 1: 完整部署（包括数据库）

适用于开发、测试环境，或者没有外部数据库的情况。

```bash
# 启动所有服务
docker-compose up -d
```

### 选项 2: 仅部署应用（使用外部数据库）

适用于生产环境，已有独立的数据库服务。

1. 修改 `.env` 文件，配置外部数据库地址：

```properties
MONGODB_HOST=your-external-mongodb-host
MYSQL_HOST=your-external-mysql-host
```

2. 仅启动应用服务：

```bash
docker-compose up -d backend frontend
```

---

## 🔧 常用操作

### 查看服务状态

```bash
docker-compose ps
```

### 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看后端日志
docker-compose logs -f backend

# 查看前端日志
docker-compose logs -f frontend
```

### 重启服务

```bash
# 重启所有服务
docker-compose restart

# 重启特定服务
docker-compose restart backend
```

### 停止服务

```bash
# 停止所有服务（保留数据）
docker-compose down

# 停止并删除所有数据（谨慎！）
docker-compose down -v
```

### 更新代码后重新部署

```bash
# 1. 停止服务
docker-compose down

# 2. 重新构建镜像
docker-compose build --no-cache

# 3. 启动服务
docker-compose up -d
```

---

## 🐛 常见问题

### 1. 端口被占用

**错误信息**: `Bind for 0.0.0.0:80 failed: port is already allocated`

**解决方案**:

修改 `docker-compose.yml` 中的端口映射，例如将前端端口改为 8000：

```yaml
frontend:
  ports:
    - "8000:80"  # 将 80:80 改为 8000:80
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

---

## 📚 更多帮助

- [DOCKER_QUICK_START.md](./DOCKER_QUICK_START.md) - 快速启动指南
- [DOCKER_DEPLOYMENT.md](./DOCKER_DEPLOYMENT.md) - 详细部署指南
- [DOCKER_README.md](./DOCKER_README.md) - Docker 文件说明

---

## ✨ 下一步

1. ✅ 访问 http://localhost 使用应用
2. ✅ 使用默认账号登录（admin / admin123）
3. ✅ 修改默认密码
4. ✅ 开始使用 PandaCoder-Vault！

---

**祝您使用愉快！** 🎉

