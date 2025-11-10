# ================================
# PandaCoder-Vault 后端 Dockerfile
# ================================
# 使用多阶段构建优化镜像大小
# 阶段1: 构建阶段 - 使用 Maven 编译项目
# 阶段2: 运行阶段 - 使用精简的 JRE 镜像运行应用

# ================================
# 阶段 1: 构建阶段
# ================================
FROM maven:3.9-eclipse-temurin-21 AS builder

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和下载依赖（利用 Docker 缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 编译打包（跳过测试以加快构建速度）
RUN mvn clean package -DskipTests -B

# ================================
# 阶段 2: 运行阶段
# ================================
FROM eclipse-temurin:21-jre-alpine

# 设置维护者信息
LABEL maintainer="PandaCoder Team"
LABEL description="PandaCoder-Vault Backend Service"
LABEL version="1.0.0"

# 安装必要的工具和时区数据
RUN apk add --no-cache tzdata curl

# 设置时区为中国时区
ENV TZ=Asia/Shanghai

# 创建应用目录和非 root 用户
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

# 从构建阶段复制 jar 文件
COPY --from=builder /app/target/vault-backend-*.jar app.jar

# 修改文件所有者
RUN chown -R appuser:appgroup /app

# 切换到非 root 用户
USER appuser

# 暴露端口
EXPOSE 8080

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/auth/test || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

