# 🚀 PandaCoder-Vault 快速参考

## 一键启动

### 方式 1：使用启动脚本（推荐）

```bash
# 启动后端（新终端窗口）
./start-backend.sh

# 启动前端（新终端窗口）
./start-frontend.sh
```

### 方式 2：手动启动

```bash
# 后端
cd backend && mvn spring-boot:run

# 前端
cd frontend && npm run dev
```

## 访问地址

- **前端**: http://localhost:5173
- **后端 API**: http://localhost:8080/api
- **API 测试**: http://localhost:8080/api/auth/test

## 默认配置

### MongoDB
- **地址**: 需要在 `.env` 文件中配置 `MONGODB_HOST`
- **端口**: 27017（可在 `.env` 文件中修改）
- **数据库**: PandaCoder（可在 `.env` 文件中修改）
- **用户名**: 需要在 `.env` 文件中配置 `MONGODB_USERNAME`
- **密码**: 需要在 `.env` 文件中配置 `MONGODB_PASSWORD`
- **认证库**: admin（可在 `.env` 文件中修改）

### JWT
- **密钥**: 需要在 `.env` 文件中配置 `JWT_SECRET`
- **有效期**: 24小时（可在 `.env` 文件中修改）

## 常用命令

### 后端

```bash
# 编译
mvn clean install

# 跳过测试编译
mvn clean install -DskipTests

# 运行
mvn spring-boot:run

# 打包
mvn package

# 运行测试
mvn test
```

### 前端

```bash
# 安装依赖
npm install --legacy-peer-deps

# 开发模式
npm run dev

# 构建生产版本
npm run build

# 预览构建
npm run preview

# 代码检查
npm run lint
```

## API 端点

### 认证
```
POST   /api/auth/register    # 注册
POST   /api/auth/login       # 登录
GET    /api/auth/test        # 测试
```

### 周报
```
GET    /api/reports/my              # 我的周报
GET    /api/reports/{id}            # 周报详情
GET    /api/reports/range           # 日期范围查询
DELETE /api/reports/{id}            # 删除周报
```

## 测试 API

### 注册用户
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

### 登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 获取周报（需要 Token）
```bash
curl -X GET "http://localhost:8080/api/reports/my?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 目录结构

```
PandaCoder-Vault/
├── backend/              # Spring Boot 后端
│   ├── src/
│   └── pom.xml
├── frontend/             # React 前端
│   ├── src/
│   └── package.json
├── README.md            # 项目介绍
├── SETUP_GUIDE.md       # 详细指南
├── START.md             # 启动指南
├── QUICK_REFERENCE.md   # 本文件
└── PROJECT_SUMMARY.md   # 项目总结
```

## 故障排查

### 后端无法启动
1. 检查 MongoDB 连接
2. 检查端口 8080 是否被占用
3. 查看控制台错误日志

### 前端无法启动
1. 删除 node_modules 重新安装
2. 检查端口 5173 是否被占用
3. 使用 `--legacy-peer-deps` 安装依赖

### 无法登录
1. 检查后端是否启动
2. 检查浏览器控制台错误
3. 确认用户名密码正确

### 看不到周报
1. 确认已在 IDEA 插件中归档周报
2. 检查 MongoDB 中是否有数据
3. 刷新页面

## 开发提示

### 后端开发
- 修改代码后自动重启（Spring Boot DevTools）
- 日志级别在 application.yml 中配置
- 使用 Lombok 减少样板代码

### 前端开发
- Vite 支持热更新
- 使用 React DevTools 调试
- Ant Design 组件文档：https://ant.design

## 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **Node.js**: 18+
- **MongoDB**: 4.4+

## 端口占用

- **8080**: 后端 API
- **5173**: 前端开发服务器
- **27017**: MongoDB（远程）

## 文件说明

- `README.md` - 项目介绍和概述
- `SETUP_GUIDE.md` - 详细的部署和配置指南
- `START.md` - 快速启动教程
- `QUICK_REFERENCE.md` - 本文件，快速参考
- `PROJECT_SUMMARY.md` - 项目完成总结
- `start-backend.sh` - 后端启动脚本
- `start-frontend.sh` - 前端启动脚本

## 技术栈

### 后端
- Spring Boot 3.3.5
- Spring Security 6.x
- Spring Data MongoDB
- JWT (jjwt 0.12.3)
- Lombok

### 前端
- React 18
- Vite 5
- Ant Design 5
- Axios
- React Router 6
- Day.js

## 下一步

1. ✅ 启动后端和前端
2. ✅ 注册一个测试账号
3. ✅ 在 IDEA 插件中归档周报
4. ✅ 在 Web 端查看周报
5. ✅ 探索其他功能

---

**快速帮助**: 遇到问题请查看 `SETUP_GUIDE.md`

