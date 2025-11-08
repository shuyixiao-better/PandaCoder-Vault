# PandaCoder-Vault 部署指南

## 📋 当前项目状态

### ✅ 已完成
1. **后端 Spring Boot 应用** - 100% 完成
   - ✅ 用户认证系统（注册、登录、JWT）
   - ✅ 周报管理 API
   - ✅ MongoDB 集成
   - ✅ 安全配置
   - ✅ CORS 配置

2. **项目结构** - 100% 完成
   - ✅ Maven 项目配置
   - ✅ 完整的后端代码
   - ✅ 配置文件

### ⏳ 进行中
1. **前端 React 应用** - 0% 完成
   - ⏳ Vite 项目初始化中（npm install 正在运行）
   - ⏳ 等待依赖安装完成

### 📝 待完成
1. **前端开发** - 需要手动完成
   - ⏳ React 组件开发
   - ⏳ 页面路由配置
   - ⏳ API 服务集成
   - ⏳ UI 界面实现

## 🚀 部署步骤

### 第一步：启动后端服务

#### 1. 配置 MongoDB 连接信息

创建并编辑文件：`backend/.env`

```properties
MONGODB_HOST=你的MongoDB服务器地址
MONGODB_PORT=27017
MONGODB_DATABASE=PandaCoder
MONGODB_USERNAME=你的用户名
MONGODB_PASSWORD=你的密码
MONGODB_AUTH_DATABASE=admin

JWT_SECRET=你的JWT密钥
JWT_EXPIRATION=86400000

SERVER_PORT=8080
```

**注意**：密码中的特殊字符（如 `@`）可以直接使用，Spring Boot 会自动处理。

#### 2. 编译并启动后端

```bash
cd /Users/shuyixiao/IdeaProjects/PandaCoder-Vault/backend
mvn clean install
mvn spring-boot:run
```

#### 3. 验证后端启动

访问：`http://localhost:8080/api/auth/test`

应该返回：
```json
{
  "success": true,
  "message": "API is working!",
  "data": null
}
```

### 第二步：完成前端项目初始化

#### 1. 等待 npm install 完成

当前 `npm create vite@latest frontend -- --template react` 命令正在运行中。

如果长时间未完成，可以手动中断并重新创建：

```bash
cd /Users/shuyixiao/IdeaProjects/PandaCoder-Vault
rm -rf frontend
npm create vite@latest frontend -- --template react
cd frontend
npm install
```

#### 2. 安装额外依赖

```bash
cd frontend
npm install axios react-router-dom antd @ant-design/icons dayjs
```

### 第三步：创建前端代码

由于前端代码较多，建议按以下顺序手动创建：

#### 1. 配置文件

**`frontend/vite.config.js`**
```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

#### 2. API 服务层

创建 `frontend/src/services/` 目录，包含：
- `axiosConfig.js` - Axios 配置和拦截器
- `authService.js` - 认证相关 API
- `reportService.js` - 周报相关 API

#### 3. 页面组件

创建 `frontend/src/pages/` 目录，包含：
- `Login.jsx` - 登录页面
- `Register.jsx` - 注册页面
- `Dashboard.jsx` - 主面板
- `WeeklyReports.jsx` - 周报列表
- `ReportDetail.jsx` - 周报详情

#### 4. 通用组件

创建 `frontend/src/components/` 目录，包含：
- `Layout.jsx` - 主布局
- `Header.jsx` - 顶部导航
- `Sidebar.jsx` - 侧边栏
- `PrivateRoute.jsx` - 路由守卫

#### 5. 工具函数

创建 `frontend/src/utils/` 目录，包含：
- `auth.js` - Token 管理
- `request.js` - 请求封装

#### 6. 路由配置

修改 `frontend/src/App.jsx` 配置路由

## 🧪 测试流程

### 1. 测试后端 API

#### 注册用户
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "nickname": "测试用户"
  }'
```

#### 登录获取 Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### 获取周报列表
```bash
curl -X GET "http://localhost:8080/api/reports/my?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 2. 测试前端应用

```bash
cd frontend
npm run dev
```

访问：`http://localhost:5173`

## ⚠️ 常见问题

### 问题 1：MongoDB 连接失败

**症状**：后端启动时报 `MongoSecurityException`

**解决方案**：
1. 检查 MongoDB 服务是否运行
2. 验证用户名密码是否正确
3. 确认认证数据库设置为 `admin`

### 问题 2：CORS 错误

**症状**：前端请求后端时报跨域错误

**解决方案**：
1. 确认后端 `SecurityConfig.java` 中 CORS 配置正确
2. 检查前端请求 URL 是否正确
3. 使用 Vite 代理配置

### 问题 3：JWT Token 过期

**症状**：登录后一段时间请求返回 401

**解决方案**：
1. 检查 `application.yml` 中 `jwt.expiration` 配置
2. 实现 Token 刷新机制
3. 前端添加 Token 过期处理逻辑

## 📊 数据库说明

### Collections

#### users
```javascript
{
  "_id": ObjectId,
  "username": String,      // 唯一
  "email": String,         // 唯一
  "password": String,      // BCrypt 加密
  "nickname": String,
  "avatar": String,
  "roles": [String],       // ["USER"]
  "enabled": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate,
  "lastLoginAt": ISODate
}
```

#### weekly_reports
```javascript
{
  "_id": ObjectId,
  "userId": String,        // 关联用户 ID
  "reportContent": String, // 周报内容
  "commits": [Object],     // 提交记录
  "generatedTime": ISODate,
  "weekStartDate": ISODate,
  "weekEndDate": ISODate,
  "projectName": String,
  "aiModel": String,
  "totalCommits": Number,
  "totalAuthors": Number,
  "metadata": Object
}
```

## 🔧 配置说明

### JWT 配置

在 `application.yml` 中：
```yaml
jwt:
  secret: ${JWT_SECRET:}  # 必须通过环境变量设置
  expiration: 86400000  # 24小时（毫秒）
```

**生产环境建议**：
- 必须在 `.env` 文件中设置 `JWT_SECRET`，使用强随机字符串
- 缩短 Token 有效期
- 实现 Refresh Token 机制

### MongoDB 配置

所有环境都通过 `.env` 文件配置：
```properties
MONGODB_HOST=你的MongoDB服务器地址
MONGODB_PORT=27017
MONGODB_DATABASE=PandaCoder
MONGODB_USERNAME=你的用户名
MONGODB_PASSWORD=你的密码
MONGODB_AUTH_DATABASE=admin
```

**重要**：
- ⚠️ 所有敏感信息必须通过 `.env` 文件配置
- ⚠️ `.env` 文件已被添加到 `.gitignore`，不会被提交到 Git
- ⚠️ 生产环境请使用不同的密码和密钥

## 📞 技术支持

如遇到问题，请检查：
1. 后端日志：`backend/logs/`
2. 前端控制台：浏览器开发者工具
3. MongoDB 日志

## 🎯 下一步行动

1. **立即执行**：
   - 启动后端服务并测试 API
   - 等待前端 npm install 完成
   
2. **短期目标**：
   - 完成前端基础框架搭建
   - 实现登录注册页面
   - 实现周报列表展示

3. **中期目标**：
   - 完善周报详情页面
   - 添加搜索和筛选功能
   - 优化用户体验

---

**最后更新**: 2025-11-08

