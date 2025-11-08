# 更新日志

## [2025-11-08] 安全性和配置改进

### 🔐 安全性改进

#### 1. 环境变量配置
- ✅ 添加了 `.env` 文件支持,用于存储敏感信息
- ✅ 创建了 `.env.example` 示例文件
- ✅ 更新了 `.gitignore`,确保 `.env` 文件不会被提交到 Git
- ✅ 添加了 `spring-dotenv` 依赖来支持 `.env` 文件读取

#### 2. 配置文件改进
- ✅ 修改 `application.yml`,支持从环境变量读取 JWT 配置
- ✅ 修改 `application-dev.yml`,支持从环境变量读取 MongoDB 配置
- ✅ 移除了配置文件中的硬编码密码

### 🐛 Bug 修复

#### 1. MongoDB 连接问题
**问题**: 后端服务启动失败,报错 `The connection string contains invalid user information`

**原因**: 密码中包含特殊字符 `@`,在 MongoDB URI 中需要 URL 编码

**解决方案**: 
- 改用分离的配置方式(host, port, username, password)
- Spring Boot 会自动处理密码中的特殊字符
- 不再需要手动 URL 编码

#### 2. JDK 版本配置
**改进**: 
- ✅ 确认项目使用 JDK 21 编译
- ✅ 更新启动脚本,自动检测并使用 JDK 21
- ✅ 兼容 JDK 24 等更高版本

#### 3. Maven 路径问题
**改进**:
- ✅ 启动脚本自动检测系统 Maven
- ✅ 如果系统没有 Maven,自动使用 IntelliJ IDEA 自带的 Maven
- ✅ 提供友好的错误提示

### 📝 文档更新

#### 1. 新增文档
- ✅ `ENV_SETUP.md` - 详细的环境配置说明
- ✅ `CHANGELOG.md` - 更新日志(本文件)
- ✅ `backend/.env.example` - 环境变量示例文件

#### 2. 更新文档
- ✅ `README.md` - 添加环境配置说明和快速启动指南
- ✅ `start-backend.sh` - 改进启动脚本,支持自动配置

### 🚀 启动脚本改进

#### start-backend.sh 新功能
1. ✅ 自动检测并使用 JDK 21
2. ✅ 检查 `.env` 文件,不存在则从 `.env.example` 复制
3. ✅ 自动查找可用的 Maven(系统 Maven 或 IDEA Maven)
4. ✅ 提供详细的启动日志和错误提示
5. ✅ 编译成功后自动启动服务

### 📦 依赖更新

#### 新增依赖
```xml
<dependency>
    <groupId>me.paulschwarz</groupId>
    <artifactId>spring-dotenv</artifactId>
    <version>4.0.0</version>
</dependency>
```

### ✅ 测试验证

- ✅ 后端服务成功启动
- ✅ MongoDB 连接成功
- ✅ API 测试通过 (`/api/auth/test`)
- ✅ JDK 21 编译和运行正常
- ✅ 环境变量配置生效

### 🔒 安全性检查清单

- [x] 密码不再硬编码在配置文件中
- [x] `.env` 文件已添加到 `.gitignore`
- [x] 提供了 `.env.example` 作为模板
- [x] 文档中说明了安全最佳实践
- [x] JWT 密钥支持从环境变量读取

### 📋 后续建议

1. **生产环境部署**
   - 使用不同的 `.env` 文件
   - 使用更强的 JWT 密钥
   - 定期更换密码和密钥

2. **进一步改进**
   - 考虑使用 Spring Cloud Config 或 Vault 管理配置
   - 添加配置加密功能
   - 实现配置热更新

3. **监控和日志**
   - 添加应用监控
   - 配置日志收集
   - 设置告警机制

---

## 使用说明

### 首次使用

1. 克隆项目后,复制环境变量文件:
```bash
cd backend
cp .env.example .env
```

2. 编辑 `.env` 文件,配置你的数据库信息

3. 启动后端服务:
```bash
./start-backend.sh
```

### 更新项目

如果你已经在使用旧版本:

1. 拉取最新代码:
```bash
git pull
```

2. 创建 `.env` 文件:
```bash
cd backend
cp .env.example .env
```

3. 将你之前在 `application-dev.yml` 中的配置迁移到 `.env` 文件

4. 重新编译和启动:
```bash
./start-backend.sh
```

---

**重要提示**: 
- ⚠️ 请勿将 `.env` 文件提交到 Git
- ⚠️ 请勿在公开场合分享你的 `.env` 文件内容
- ⚠️ 定期更换密码和密钥以确保安全

