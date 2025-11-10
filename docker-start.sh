#!/bin/bash

# ================================
# PandaCoder-Vault Docker 快速启动脚本
# ================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# 打印标题
print_header() {
    echo ""
    echo "╔═══════════════════════════════════════════════════════════╗"
    echo "║                                                           ║"
    echo "║   🐼 PandaCoder-Vault Docker 快速启动脚本 🐼            ║"
    echo "║                                                           ║"
    echo "╚═══════════════════════════════════════════════════════════╝"
    echo ""
}

# 检查 Docker 是否安装
check_docker() {
    print_info "检查 Docker 是否安装..."
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装，请先安装 Docker"
        echo "访问 https://docs.docker.com/get-docker/ 获取安装指南"
        exit 1
    fi
    print_success "Docker 已安装: $(docker --version)"
}

# 检查 Docker Compose 是否安装
check_docker_compose() {
    print_info "检查 Docker Compose 是否安装..."
    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
        print_error "Docker Compose 未安装，请先安装 Docker Compose"
        echo "访问 https://docs.docker.com/compose/install/ 获取安装指南"
        exit 1
    fi
    
    # 检测使用哪个命令
    if command -v docker-compose &> /dev/null; then
        DOCKER_COMPOSE_CMD="docker-compose"
        print_success "Docker Compose 已安装: $(docker-compose --version)"
    else
        DOCKER_COMPOSE_CMD="docker compose"
        print_success "Docker Compose 已安装: $(docker compose version)"
    fi
}

# 检查并创建 .env 文件
check_env_file() {
    print_info "检查环境变量配置文件..."
    if [ ! -f ".env" ]; then
        print_warning ".env 文件不存在"
        if [ -f ".env.docker.example" ]; then
            print_info "从 .env.docker.example 复制配置文件..."
            cp .env.docker.example .env
            print_success "已创建 .env 文件"
            print_warning "请编辑 .env 文件，修改数据库密码和 JWT 密钥！"
            echo ""
            read -p "是否现在编辑 .env 文件？(y/n) " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                ${EDITOR:-vi} .env
            fi
        else
            print_error ".env.docker.example 文件也不存在！"
            exit 1
        fi
    else
        print_success ".env 文件已存在"
    fi
}

# 生成 JWT 密钥
generate_jwt_secret() {
    print_info "检查 JWT 密钥配置..."
    
    # 读取当前 JWT_SECRET
    if [ -f ".env" ]; then
        source .env
        if [ -z "$JWT_SECRET" ] || [ "$JWT_SECRET" = "your-jwt-secret-key-change-this-in-production" ]; then
            print_warning "检测到默认或空的 JWT 密钥"
            read -p "是否自动生成安全的 JWT 密钥？(y/n) " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                NEW_SECRET=$(openssl rand -base64 64 | tr -d '\n')
                # 使用 sed 替换 JWT_SECRET
                if [[ "$OSTYPE" == "darwin"* ]]; then
                    # macOS
                    sed -i '' "s|JWT_SECRET=.*|JWT_SECRET=$NEW_SECRET|" .env
                else
                    # Linux
                    sed -i "s|JWT_SECRET=.*|JWT_SECRET=$NEW_SECRET|" .env
                fi
                print_success "已生成并保存新的 JWT 密钥"
            fi
        else
            print_success "JWT 密钥已配置"
        fi
    fi
}

# 构建镜像
build_images() {
    print_info "构建 Docker 镜像..."
    $DOCKER_COMPOSE_CMD build
    print_success "镜像构建完成"
}

# 启动服务
start_services() {
    print_info "启动服务..."
    $DOCKER_COMPOSE_CMD up -d
    print_success "服务启动完成"
}

# 等待服务就绪
wait_for_services() {
    print_info "等待服务就绪..."
    echo ""
    
    # 等待后端服务
    print_info "等待后端服务启动（最多等待 60 秒）..."
    for i in {1..60}; do
        if curl -s http://localhost:8080/api/auth/test > /dev/null 2>&1; then
            print_success "后端服务已就绪"
            break
        fi
        echo -n "."
        sleep 1
    done
    echo ""
    
    # 等待前端服务
    print_info "等待前端服务启动（最多等待 30 秒）..."
    for i in {1..30}; do
        if curl -s http://localhost > /dev/null 2>&1; then
            print_success "前端服务已就绪"
            break
        fi
        echo -n "."
        sleep 1
    done
    echo ""
}

# 显示服务状态
show_status() {
    echo ""
    print_info "服务状态："
    $DOCKER_COMPOSE_CMD ps
    echo ""
}

# 显示访问信息
show_access_info() {
    echo ""
    echo "╔═══════════════════════════════════════════════════════════╗"
    echo "║                                                           ║"
    echo "║   🎉 PandaCoder-Vault 启动成功！                         ║"
    echo "║                                                           ║"
    echo "║   📱 前端地址:    http://localhost                       ║"
    echo "║   🔌 后端 API:    http://localhost:8080/api              ║"
    echo "║   🏥 健康检查:    http://localhost:8080/api/auth/test    ║"
    echo "║                                                           ║"
    echo "║   👤 默认账号:    admin / admin123                       ║"
    echo "║                                                           ║"
    echo "╚═══════════════════════════════════════════════════════════╝"
    echo ""
    print_info "查看日志: $DOCKER_COMPOSE_CMD logs -f"
    print_info "停止服务: $DOCKER_COMPOSE_CMD down"
    echo ""
}

# 主函数
main() {
    print_header
    
    check_docker
    check_docker_compose
    check_env_file
    generate_jwt_secret
    
    echo ""
    read -p "是否开始构建和启动服务？(y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_warning "已取消启动"
        exit 0
    fi
    
    build_images
    start_services
    wait_for_services
    show_status
    show_access_info
}

# 运行主函数
main

