import { auth } from './auth.js'

let tokenCheckInterval = null
let isCheckerRunning = false

/**
 * Token过期检测器
 * 提供主动检测token是否过期的功能，适用于内嵌场景
 */
export const tokenChecker = {
  // 启动token检测器
  start(checkInterval = 60000) { // 默认每分钟检查一次
    if (isCheckerRunning) {
      console.warn('Token检测器已在运行')
      return
    }

    isCheckerRunning = true
    console.log('Token检测器已启动')

    // 立即执行一次检查
    this.checkToken()

    // 设置定期检查
    tokenCheckInterval = setInterval(() => {
      this.checkToken()
    }, checkInterval)
  },

  // 停止token检测器
  stop() {
    if (tokenCheckInterval) {
      clearInterval(tokenCheckInterval)
      tokenCheckInterval = null
      isCheckerRunning = false
      console.log('Token检测器已停止')
    }
  },

  // 检查token是否过期
  checkToken() {
    // 如果没有token，直接返回
    if (!auth.getToken()) {
      return false
    }

    // 如果token已过期，重定向到登录页
    if (auth.isTokenExpired()) {
      console.warn('Token已过期，重定向到登录页')
      auth.redirectToLogin()
      return false
    }

    // 检查token即将过期（剩余时间少于5分钟）
    const remainingTime = auth.getTokenRemainingTime()
    if (remainingTime > 0 && remainingTime < 5 * 60 * 1000) {
      console.warn('Token即将过期，剩余时间:', Math.round(remainingTime / 1000), '秒')
      // 这里可以添加通知逻辑，提醒用户token即将过期
    }

    return true
  },

  // 获取检测器状态
  isRunning() {
    return isCheckerRunning
  },

  // 手动触发token检查
  forceCheck() {
    return this.checkToken()
  }
}

// 页面卸载时清理检测器
window.addEventListener('beforeunload', () => {
  tokenChecker.stop()
})