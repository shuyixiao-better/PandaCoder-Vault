#!/bin/bash

echo "🚀 启动 PandaCoder-Vault 后端服务..."
echo ""

# 设置 JDK 21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
echo "☕ 使用 Java 版本: $(java -version 2>&1 | head -n 1)"
echo ""

cd backend

# 检查 .env 文件是否存在
if [ ! -f ".env" ]; then
    echo "⚠️  .env 文件不存在，从 .env.example 复制..."
    if [ -f ".env.example" ]; then
        cp .env.example .env
        echo "✅ 已创建 .env 文件，请根据需要修改配置"
    else
        echo "❌ .env.example 文件也不存在！"
        exit 1
    fi
fi

# 设置 Maven 路径
MVN_CMD=""
if command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
    echo "✅ 使用系统 Maven"
elif [ -f "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn" ]; then
    MVN_CMD="/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn"
    echo "✅ 使用 IntelliJ IDEA 自带的 Maven"
else
    echo "❌ Maven 未找到，请安装 Maven 或使用 IntelliJ IDEA"
    exit 1
fi

echo ""
echo "🔨 编译项目..."
$MVN_CMD clean install -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 编译成功！"
    echo ""
    echo "🚀 启动 Spring Boot 应用..."
    echo ""
    $MVN_CMD spring-boot:run
else
    echo ""
    echo "❌ 编译失败，请检查错误信息"
    exit 1
fi

