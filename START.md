# 🚀 PandaCoder-Vault 启动指南

## ✅ 项目已完成

恭喜！PandaCoder-Vault 项目已经创建完成。以下是项目的完整结构和启动步骤。

## 📦 项目结构

```
PandaCoder-Vault/
├── backend/                    # Spring Boot 后端 ✅ 完成
│   ├── src/main/java/
│   │   └── com/pandacoder/vault/
│   │       ├── config/         # 配置类（Security, MongoDB）
│   │       ├── controller/     # REST API 控制器
│   │       ├── service/        # 业务逻辑层
│   │       ├── repository/     # 数据访问层
│   │       ├── model/          # 数据模型
│   │       ├── dto/            # 数据传输对象
│   │       ├── security/       # 安全相关
│   │       └── util/           # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── application-dev.yml
│   └── pom.xml
│
├── frontend/                   # React 前端 ✅ 完成
│   ├── src/
│   │   ├── components/         # 通用组件
│   │   ├── pages/              # 页面组件
│   │   ├── services/           # API 服务
│   │   ├── utils/              # 工具函数
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
│
├── README.md                   # 项目说明
├── SETUP_GUIDE.md             # 详细部署指南
└── START.md                   # 本文件
```

## 🎯 核心功能

### 已实现功能
1. ✅ 用户注册和登录（JWT 认证）
2. ✅ 查看个人 Git 周报列表（分页）
3. ✅ 查看周报详情
4. ✅ 删除周报
5. ✅ 与 PandaCoder IDEA 插件集成

### 技术栈
- **后端**: Spring Boot 3.3.5 + MongoDB + JWT
- **前端**: React 18 + Vite + Ant Design + Axios
- **数据库**: MongoDB (共享 IDEA 插件数据库)

## 🚀 快速启动

### 第一步：启动后端

```bash
# 1. 进入后端目录
cd PandaCoder-Vault/backend

# 2. 编译项目
mvn clean install

# 3. 启动后端服务
mvn spring-boot:run
```

后端将在 `http://localhost:8080` 启动

**验证后端**：
```bash
curl http://localhost:8080/api/auth/test
```

应该返回：
```json
{
  "success": true,
  "message": "API is working!",
  "data": null
}
```

### 第二步：启动前端

打开新的终端窗口：

```bash
# 1. 进入前端目录
cd PandaCoder-Vault/frontend

# 2. 安装依赖（如果还没安装）
npm install --legacy-peer-deps

# 3. 启动开发服务器
npm run dev
```

前端将在 `http://localhost:5173` 启动

### 第三步：访问应用

在浏览器中打开：`http://localhost:5173`

## 📝 使用流程

### 1. 注册账号
1. 访问 `http://localhost:5173`
2. 点击"立即注册"
3. 填写用户名、邮箱、昵称和密码
4. 点击"注册"按钮

### 2. 登录系统
1. 使用注册的用户名和密码登录
2. 登录成功后会自动跳转到主页面

### 3. 查看周报
1. 左侧菜单点击"Git 周报"
2. 查看周报列表（分页显示）
3. 点击"查看"按钮查看周报详情
4. 可以删除不需要的周报

### 4. 使用 IDEA 插件归档周报
1. 在 IDEA 中使用 PandaCoder 插件生成周报
2. 点击"归档周报"按钮
3. 周报会自动保存到 MongoDB
4. 在 Web 端刷新即可看到新的周报

## ⚠️ 常见问题

### 问题 1：后端启动失败 - MongoDB 连接错误

**解决方案**：
检查 `backend/.env` 文件中的 MongoDB 配置：
```properties
MONGODB_HOST=你的MongoDB服务器地址
MONGODB_PORT=27017
MONGODB_DATABASE=PandaCoder
MONGODB_USERNAME=你的用户名
MONGODB_PASSWORD=你的密码
MONGODB_AUTH_DATABASE=admin
```

确保：
- `.env` 文件已创建并配置正确
- MongoDB 服务正在运行
- 用户名密码正确
- 网络连接正常

### 问题 2：前端 npm install 失败

**解决方案**：
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install --legacy-peer-deps
```

### 问题 3：前端无法连接后端

**解决方案**：
1. 确认后端已启动（`http://localhost:8080/api/auth/test`）
2. 检查浏览器控制台是否有 CORS 错误
3. 确认 Vite 代理配置正确（`frontend/vite.config.js`）

### 问题 4：登录后看不到周报

**原因**：IDEA 插件归档的周报没有 `userId` 字段

**解决方案**：
1. 使用当前账号重新在 IDEA 插件中归档周报
2. 或者手动在 MongoDB 中为现有周报添加 `userId` 字段

## 🔧 开发建议

### 后端开发
```bash
# 运行测试
cd backend
mvn test

# 打包
mvn package

# 跳过测试打包
mvn package -DskipTests
```

### 前端开发
```bash
# 开发模式（热更新）
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview

# 代码检查
npm run lint
```

## 📚 API 文档

### 认证相关
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/test` - API 健康检查

### 周报相关
- `GET /api/reports/my?page=0&size=10` - 获取我的周报列表
- `GET /api/reports/{id}` - 获取周报详情
- `GET /api/reports/range?startDate=...&endDate=...` - 按日期范围查询
- `DELETE /api/reports/{id}` - 删除周报

## 🎨 界面预览

### 登录页面
- 渐变背景
- 简洁的登录表单
- 注册链接

### 主页面
- 顶部导航栏（Logo + 用户信息）
- 左侧菜单（Git 周报、知识笔记等）
- 主内容区域

### 周报列表
- 表格展示
- 分页功能
- 查看和删除操作

### 周报详情
- 基本信息展示
- 周报内容
- 提交记录时间线

## 🔐 安全说明

### JWT Token
- 有效期：24小时
- 存储位置：localStorage
- 自动添加到请求头

### 密码安全
- 使用 BCrypt 加密
- 最小长度：6个字符

### CORS 配置
- 允许来源：`http://localhost:5173`, `http://localhost:3000`
- 允许方法：GET, POST, PUT, DELETE
- 允许凭证：是

## 📈 后续规划

### v1.1.0
- [ ] 知识笔记功能
- [ ] Markdown 编辑器
- [ ] 代码片段管理
- [ ] 标签系统

### v2.0.0
- [ ] 项目管理模块
- [ ] 资源收藏功能
- [ ] 数据导出
- [ ] 移动端适配

## 💡 提示

1. **首次使用**：建议先注册一个测试账号，然后在 IDEA 插件中归档一些周报进行测试
2. **开发调试**：打开浏览器开发者工具查看网络请求和控制台日志
3. **数据库查看**：可以使用 MongoDB Compass 连接数据库查看数据
4. **日志查看**：后端日志在控制台输出，前端日志在浏览器控制台

## 📞 获取帮助

如果遇到问题：
1. 查看 `SETUP_GUIDE.md` 获取详细的部署指南
2. 检查后端控制台日志
3. 检查浏览器开发者工具的控制台和网络标签
4. 确认 MongoDB 连接正常

---

**祝您使用愉快！🎉**

如有问题，请参考 `SETUP_GUIDE.md` 或查看项目文档。

