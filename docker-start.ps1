# ================================
# PandaCoder-Vault Docker 快速启动脚本 (Windows PowerShell)
# ================================

# 设置错误时停止
$ErrorActionPreference = "Stop"

# 打印带颜色的消息
function Print-Info {
    param([string]$Message)
    Write-Host "ℹ️  $Message" -ForegroundColor Blue
}

function Print-Success {
    param([string]$Message)
    Write-Host "✅ $Message" -ForegroundColor Green
}

function Print-Warning {
    param([string]$Message)
    Write-Host "⚠️  $Message" -ForegroundColor Yellow
}

function Print-Error {
    param([string]$Message)
    Write-Host "❌ $Message" -ForegroundColor Red
}

# 打印标题
function Print-Header {
    Write-Host ""
    Write-Host "╔═══════════════════════════════════════════════════════════╗"
    Write-Host "║                                                           ║"
    Write-Host "║   🐼 PandaCoder-Vault Docker 快速启动脚本 🐼            ║"
    Write-Host "║                                                           ║"
    Write-Host "╚═══════════════════════════════════════════════════════════╝"
    Write-Host ""
}

# 检查 Docker 是否安装
function Check-Docker {
    Print-Info "检查 Docker 是否安装..."
    try {
        $dockerVersion = docker --version
        Print-Success "Docker 已安装: $dockerVersion"
    }
    catch {
        Print-Error "Docker 未安装，请先安装 Docker Desktop"
        Write-Host "访问 https://docs.docker.com/desktop/install/windows-install/ 获取安装指南"
        exit 1
    }
}

# 检查 Docker Compose 是否安装
function Check-DockerCompose {
    Print-Info "检查 Docker Compose 是否安装..."
    try {
        $composeVersion = docker-compose --version
        Print-Success "Docker Compose 已安装: $composeVersion"
        return "docker-compose"
    }
    catch {
        try {
            $composeVersion = docker compose version
            Print-Success "Docker Compose 已安装: $composeVersion"
            return "docker compose"
        }
        catch {
            Print-Error "Docker Compose 未安装，请先安装 Docker Compose"
            Write-Host "访问 https://docs.docker.com/compose/install/ 获取安装指南"
            exit 1
        }
    }
}

# 检查并创建 .env 文件
function Check-EnvFile {
    Print-Info "检查环境变量配置文件..."
    if (-not (Test-Path ".env")) {
        Print-Warning ".env 文件不存在"
        if (Test-Path ".env.docker.example") {
            Print-Info "从 .env.docker.example 复制配置文件..."
            Copy-Item ".env.docker.example" ".env"
            Print-Success "已创建 .env 文件"
            Print-Warning "请编辑 .env 文件，修改数据库密码和 JWT 密钥！"
            Write-Host ""
            $reply = Read-Host "是否现在编辑 .env 文件？(y/n)"
            if ($reply -eq "y" -or $reply -eq "Y") {
                notepad .env
            }
        }
        else {
            Print-Error ".env.docker.example 文件也不存在！"
            exit 1
        }
    }
    else {
        Print-Success ".env 文件已存在"
    }
}

# 生成 JWT 密钥
function Generate-JwtSecret {
    Print-Info "检查 JWT 密钥配置..."
    
    if (Test-Path ".env") {
        $envContent = Get-Content ".env" -Raw
        if ($envContent -match "JWT_SECRET=(.*)") {
            $jwtSecret = $matches[1].Trim()
            if ([string]::IsNullOrEmpty($jwtSecret) -or $jwtSecret -eq "your-jwt-secret-key-change-this-in-production") {
                Print-Warning "检测到默认或空的 JWT 密钥"
                $reply = Read-Host "是否自动生成安全的 JWT 密钥？(y/n)"
                if ($reply -eq "y" -or $reply -eq "Y") {
                    # 生成随机密钥
                    $bytes = New-Object byte[] 64
                    $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
                    $rng.GetBytes($bytes)
                    $newSecret = [Convert]::ToBase64String($bytes)
                    
                    # 替换 JWT_SECRET
                    $envContent = $envContent -replace "JWT_SECRET=.*", "JWT_SECRET=$newSecret"
                    Set-Content ".env" $envContent
                    Print-Success "已生成并保存新的 JWT 密钥"
                }
            }
            else {
                Print-Success "JWT 密钥已配置"
            }
        }
    }
}

# 构建镜像
function Build-Images {
    param([string]$ComposeCmd)
    Print-Info "构建 Docker 镜像..."
    if ($ComposeCmd -eq "docker-compose") {
        docker-compose build
    }
    else {
        docker compose build
    }
    Print-Success "镜像构建完成"
}

# 启动服务
function Start-Services {
    param([string]$ComposeCmd)
    Print-Info "启动服务..."
    if ($ComposeCmd -eq "docker-compose") {
        docker-compose up -d
    }
    else {
        docker compose up -d
    }
    Print-Success "服务启动完成"
}

# 等待服务就绪
function Wait-ForServices {
    Print-Info "等待服务就绪..."
    Write-Host ""
    
    # 等待后端服务
    Print-Info "等待后端服务启动（最多等待 60 秒）..."
    for ($i = 1; $i -le 60; $i++) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/test" -UseBasicParsing -TimeoutSec 1
            if ($response.StatusCode -eq 200) {
                Print-Success "后端服务已就绪"
                break
            }
        }
        catch {
            Write-Host "." -NoNewline
            Start-Sleep -Seconds 1
        }
    }
    Write-Host ""
    
    # 等待前端服务
    Print-Info "等待前端服务启动（最多等待 30 秒）..."
    for ($i = 1; $i -le 30; $i++) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost" -UseBasicParsing -TimeoutSec 1
            if ($response.StatusCode -eq 200) {
                Print-Success "前端服务已就绪"
                break
            }
        }
        catch {
            Write-Host "." -NoNewline
            Start-Sleep -Seconds 1
        }
    }
    Write-Host ""
}

# 显示服务状态
function Show-Status {
    param([string]$ComposeCmd)
    Write-Host ""
    Print-Info "服务状态："
    if ($ComposeCmd -eq "docker-compose") {
        docker-compose ps
    }
    else {
        docker compose ps
    }
    Write-Host ""
}

# 显示访问信息
function Show-AccessInfo {
    Write-Host ""
    Write-Host "╔═══════════════════════════════════════════════════════════╗"
    Write-Host "║                                                           ║"
    Write-Host "║   🎉 PandaCoder-Vault 启动成功！                         ║"
    Write-Host "║                                                           ║"
    Write-Host "║   📱 前端地址:    http://localhost                       ║"
    Write-Host "║   🔌 后端 API:    http://localhost:8080/api              ║"
    Write-Host "║   🏥 健康检查:    http://localhost:8080/api/auth/test    ║"
    Write-Host "║                                                           ║"
    Write-Host "║   👤 默认账号:    admin / admin123                       ║"
    Write-Host "║                                                           ║"
    Write-Host "╚═══════════════════════════════════════════════════════════╝"
    Write-Host ""
    Print-Info "查看日志: docker-compose logs -f"
    Print-Info "停止服务: docker-compose down"
    Write-Host ""
}

# 主函数
function Main {
    Print-Header
    
    Check-Docker
    $composeCmd = Check-DockerCompose
    Check-EnvFile
    Generate-JwtSecret
    
    Write-Host ""
    $reply = Read-Host "是否开始构建和启动服务？(y/n)"
    if ($reply -ne "y" -and $reply -ne "Y") {
        Print-Warning "已取消启动"
        exit 0
    }
    
    Build-Images -ComposeCmd $composeCmd
    Start-Services -ComposeCmd $composeCmd
    Wait-ForServices
    Show-Status -ComposeCmd $composeCmd
    Show-AccessInfo
}

# 运行主函数
Main

