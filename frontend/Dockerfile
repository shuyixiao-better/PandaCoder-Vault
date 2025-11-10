# ================================
# PandaCoder-Vault 前端 Dockerfile
# ================================
# 使用多阶段构建优化镜像大小
# 阶段1: 构建阶段 - 使用 Node.js 构建 React 应用
# 阶段2: 运行阶段 - 使用 Nginx 提供静态文件服务

# ================================
# 阶段 1: 构建阶段
# ================================
FROM node:18-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 package-lock.json（利用 Docker 缓存层）
COPY package*.json ./

# 安装依赖
RUN npm ci --legacy-peer-deps

# 复制源代码
COPY . .

# 构建生产版本
RUN npm run build

# ================================
# 阶段 2: 运行阶段
# ================================
FROM nginx:alpine

# 设置维护者信息
LABEL maintainer="PandaCoder Team"
LABEL description="PandaCoder-Vault Frontend Service"
LABEL version="1.0.0"

# 安装 tzdata 用于时区设置
RUN apk add --no-cache tzdata

# 设置时区为中国时区
ENV TZ=Asia/Shanghai

# 从构建阶段复制构建产物到 Nginx 目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost/ || exit 1

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]

