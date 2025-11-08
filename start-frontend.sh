#!/bin/bash

echo "🚀 启动 PandaCoder-Vault 前端服务..."
echo ""

cd frontend

echo "📦 检查 Node.js 是否安装..."
if ! command -v node &> /dev/null; then
    echo "❌ Node.js 未安装，请先安装 Node.js"
    exit 1
fi

echo "✅ Node.js 已安装: $(node --version)"
echo ""

echo "📦 检查依赖是否已安装..."
if [ ! -d "node_modules" ]; then
    echo "📥 安装依赖..."
    npm install --legacy-peer-deps
    
    if [ $? -ne 0 ]; then
        echo ""
        echo "❌ 依赖安装失败，请检查错误信息"
        exit 1
    fi
fi

echo "✅ 依赖已就绪"
echo ""

echo "🚀 启动 Vite 开发服务器..."
echo ""
npm run dev

