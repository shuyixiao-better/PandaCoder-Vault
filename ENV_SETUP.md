# 环境配置说明

## 🔐 安全配置

为了保护敏感信息(如数据库密码、JWT密钥等),本项目使用 `.env` 文件来管理环境变量。

### 配置步骤

#### 1. 创建 .env 文件

在 `backend` 目录下已经有一个 `.env.example` 示例文件,你需要复制它并根据实际情况修改:

```bash
cd backend
cp .env.example .env
```

#### 2. 编辑 .env 文件

打开 `backend/.env` 文件,修改以下配置:

```properties
# MongoDB 配置
MONGODB_HOST=你的MongoDB服务器地址
MONGODB_PORT=27017
MONGODB_DATABASE=数据库名称
MONGODB_USERNAME=用户名
MONGODB_PASSWORD=密码
MONGODB_AUTH_DATABASE=admin

# JWT 配置
JWT_SECRET=你的JWT密钥(建议使用长随机字符串)
JWT_EXPIRATION=86400000

# 服务器配置
SERVER_PORT=8080
```

**重要提示**:
- ⚠️ `.env` 文件已经被添加到 `.gitignore`,不会被提交到 Git 仓库
- ⚠️ 请勿将 `.env` 文件分享给他人或上传到公开仓库
- ⚠️ 密码中如果包含特殊字符(如 `@`),在 `.env` 文件中直接写原始密码即可,Spring Boot 会自动处理

#### 3. 重要提示

⚠️ **必须创建 `.env` 文件并配置环境变量，否则应用将无法启动！**

配置文件中的敏感信息默认值已被移除，所有敏感信息必须通过环境变量提供。

## 🚀 启动后端服务

### 方式 1: 使用启动脚本(推荐)

```bash
./start-backend.sh
```

这个脚本会:
- ✅ 自动检测并使用 JDK 21
- ✅ 检查 .env 文件是否存在,不存在则从 .env.example 复制
- ✅ 使用系统 Maven 或 IntelliJ IDEA 自带的 Maven
- ✅ 编译并启动后端服务

### 方式 2: 手动启动

#### 设置 JDK 21

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

#### 编译项目

```bash
cd backend
mvn clean package -DskipTests
```

#### 运行服务

```bash
java -jar target/vault-backend-1.0.0.jar
```

## ✅ 验证服务

启动成功后,访问以下地址验证:

```bash
curl http://localhost:8080/api/auth/test
```

应该返回:
```json
{"code":200,"message":"操作成功","data":"API is working!"}
```

或在浏览器中访问: http://localhost:8080/api/auth/test

## 🔧 JDK 版本说明

本项目配置使用 **JDK 21**,但也兼容更高版本(如 JDK 24)。

### 查看系统中可用的 JDK 版本

```bash
/usr/libexec/java_home -V
```

### 切换 JDK 版本

临时切换(仅当前终端会话):
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

永久切换(添加到 `~/.bash_profile` 或 `~/.zshrc`):
```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
source ~/.zshrc
```

## 📝 配置文件说明

### application.yml (主配置)
- 定义应用名称
- 激活的配置文件(dev/prod)
- JWT 配置(从环境变量读取)
- 服务器基础配置

### application-dev.yml (开发环境配置)
- MongoDB 连接配置(从环境变量读取)
- 开发环境日志级别
- 开发环境特定配置

### .env (环境变量配置)
- 存储敏感信息
- 不会被提交到 Git
- 每个开发者可以有自己的配置

## 🛡️ 安全最佳实践

1. **永远不要**将 `.env` 文件提交到 Git
2. **永远不要**在代码中硬编码密码或密钥
3. **定期更换** JWT 密钥和数据库密码
4. **使用强密码**,包含大小写字母、数字和特殊字符
5. **生产环境**使用不同的密码和密钥

## 🐛 常见问题

### 问题 1: MongoDB 连接失败

**错误信息**: `The connection string contains invalid user information`

**原因**: 密码中包含特殊字符(如 `@`, `:`)

**解决方案**: 
- 在 `.env` 文件中直接使用原始密码,Spring Boot 会自动处理
- 例如: `MONGODB_PASSWORD=your_password_here@123`

### 问题 2: 找不到 Maven

**错误信息**: `mvn: command not found`

**解决方案**:
1. 使用启动脚本 `./start-backend.sh`,它会自动查找 IntelliJ IDEA 自带的 Maven
2. 或者安装 Maven: `brew install maven`

### 问题 3: JDK 版本不匹配

**错误信息**: `Unsupported class file major version`

**解决方案**:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

## 📞 技术支持

如遇到其他问题,请检查:
1. `.env` 文件是否正确配置
2. MongoDB 服务是否正常运行
3. JDK 版本是否正确
4. 端口 8080 是否被占用

---

**祝你使用愉快! 🐼**

