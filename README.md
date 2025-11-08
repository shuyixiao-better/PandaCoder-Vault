# PandaCoder-Vault 🐼

> 程序员的个人知识库 - Your Personal Knowledge Vault for Developers

## 📖 项目简介

PandaCoder-Vault 是一个面向程序员的个人知识管理系统，帮助开发者更好地管理和沉淀技术知识、项目经验和工作成果。

### 核心功能模块

#### 1. 📊 Git 周报管理
- 自动归档来自 PandaCoder IDEA 插件的 Git 周报
- 可视化展示个人代码贡献统计
- 支持周报检索和导出

#### 2. 📝 知识库管理（规划中）
- Markdown 笔记管理
- 代码片段收藏
- 技术文档归档
- 标签分类和全文搜索

#### 3. 🎯 项目管理（规划中）
- 个人项目档案
- 技术栈记录
- 项目经验总结

#### 4. 🔖 资源收藏（规划中）
- 技术文章收藏
- 学习资源管理
- 书签同步

## 🏗️ 技术架构

### 后端技术栈
- **框架**: Spring Boot 3.3.5
- **数据库**: MongoDB
- **认证**: JWT Token
- **构建工具**: Maven
- **Java 版本**: JDK 17+

### 前端技术栈
- **框架**: React 18
- **UI 库**: Ant Design / Material-UI
- **状态管理**: Redux Toolkit / Zustand
- **路由**: React Router v6
- **HTTP 客户端**: Axios
- **构建工具**: Vite

## 📁 项目结构

```
PandaCoder-Vault/
├── backend/                 # Spring Boot 后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/pandacoder/vault/
│   │   │   │       ├── config/          # 配置类
│   │   │   │       ├── controller/      # 控制器
│   │   │   │       ├── service/         # 业务逻辑
│   │   │   │       ├── repository/      # 数据访问
│   │   │   │       ├── model/           # 数据模型
│   │   │   │       ├── dto/             # 数据传输对象
│   │   │   │       ├── security/        # 安全配置
│   │   │   │       └── util/            # 工具类
│   │   │   └── resources/
│   │   │       ├── application.yml      # 应用配置
│   │   │       └── application-dev.yml  # 开发环境配置
│   │   └── test/
│   └── pom.xml
│
├── frontend/                # React 前端
│   ├── public/
│   ├── src/
│   │   ├── components/      # 通用组件
│   │   ├── pages/           # 页面组件
│   │   ├── services/        # API 服务
│   │   ├── store/           # 状态管理
│   │   ├── utils/           # 工具函数
│   │   ├── hooks/           # 自定义 Hooks
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
│
├── docs/                    # 项目文档
├── .gitignore
└── README.md
```

## 🚀 快速开始

### 前置要求

- **JDK 21** (推荐) 或更高版本
- Node.js 18 或更高版本
- MongoDB 4.4 或更高版本
- Maven 3.6+ (可选,可使用 IntelliJ IDEA 自带的 Maven)

### 🔐 环境配置 (重要!)

**首次启动前,请先配置环境变量:**

1. 复制环境变量示例文件:
```bash
cd backend
cp .env.example .env
```

2. 编辑 `backend/.env` 文件,配置你的 MongoDB 连接信息:
```properties
MONGODB_HOST=你的MongoDB地址
MONGODB_PORT=27017
MONGODB_DATABASE=PandaCoder
MONGODB_USERNAME=你的用户名
MONGODB_PASSWORD=你的密码
MONGODB_AUTH_DATABASE=admin

JWT_SECRET=你的JWT密钥
```

**详细配置说明请查看**: [ENV_SETUP.md](./ENV_SETUP.md)

### 后端启动

**方式 1: 使用启动脚本 (推荐)**
```bash
./start-backend.sh
```

**方式 2: 手动启动**
```bash
# 设置 JDK 21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# 编译并运行
cd backend
mvn clean package -DskipTests
java -jar target/vault-backend-1.0.0.jar
```

后端服务将在 `http://localhost:8080/api` 启动

**验证服务**:
```bash
curl http://localhost:8080/api/auth/test
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端应用将在 `http://localhost:5173` 启动

## 🔐 默认账号

首次启动后，系统会自动创建管理员账号：
- 用户名: `admin`
- 密码: `admin123`

**⚠️ 请在首次登录后立即修改密码！**

## 📝 开发计划

### v1.0.0 (当前版本)
- [x] 项目初始化
- [x] 用户注册/登录
- [x] JWT 认证
- [x] Git 周报查看
- [ ] 周报详情展示
- [ ] 周报搜索功能

### v1.1.0 (规划中)
- [ ] 知识库笔记功能
- [ ] Markdown 编辑器
- [ ] 代码片段管理
- [ ] 标签系统

### v2.0.0 (规划中)
- [ ] 项目管理模块
- [ ] 资源收藏功能
- [ ] 数据导出功能
- [ ] 移动端适配

## 🤝 配合 PandaCoder IDEA 插件使用

1. 在 IDEA 插件中配置 MongoDB 连接信息
2. 使用插件生成 Git 周报
3. 点击"归档周报"按钮，周报将自动保存到 MongoDB
4. 在 PandaCoder-Vault Web 端查看和管理所有周报

## 📄 开源协议

MIT License

## 👨‍💻 作者

PandaCoder Team

---

**⭐ 如果这个项目对你有帮助，请给个 Star！**

