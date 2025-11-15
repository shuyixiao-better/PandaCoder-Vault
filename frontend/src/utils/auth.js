const TOKEN_KEY = 'pandacoder_token'
const USER_KEY = 'pandacoder_user'
const TOKEN_EXPIRY_KEY = 'pandacoder_token_expiry'

export const auth = {
  // 保存 Token
  setToken(token, expiresIn = 86400000) { // 默认24小时，单位毫秒
    localStorage.setItem(TOKEN_KEY, token)
    // 计算并存储过期时间戳
    const expiryTime = Date.now() + expiresIn
    localStorage.setItem(TOKEN_EXPIRY_KEY, expiryTime.toString())
  },

  // 获取 Token
  getToken() {
    return localStorage.getItem(TOKEN_KEY)
  },

  // 删除 Token
  removeToken() {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(TOKEN_EXPIRY_KEY)
  },

  // 保存用户信息
  setUser(user) {
    localStorage.setItem(USER_KEY, JSON.stringify(user))
  },

  // 获取用户信息
  getUser() {
    const userStr = localStorage.getItem(USER_KEY)
    return userStr ? JSON.parse(userStr) : null
  },

  // 删除用户信息
  removeUser() {
    localStorage.removeItem(USER_KEY)
  },

  // 检查是否已登录
  isAuthenticated() {
    return !!this.getToken() && !this.isTokenExpired()
  },

  // 检查token是否过期
  isTokenExpired() {
    const expiryTime = localStorage.getItem(TOKEN_EXPIRY_KEY)
    if (!expiryTime) {
      // 如果没有过期时间，说明是旧版本token，需要重新登录
      return true
    }
    return Date.now() > parseInt(expiryTime, 10)
  },

  // 获取token剩余有效时间（毫秒）
  getTokenRemainingTime() {
    const expiryTime = localStorage.getItem(TOKEN_EXPIRY_KEY)
    if (!expiryTime) return 0
    return Math.max(0, parseInt(expiryTime, 10) - Date.now())
  },

  // 登出
  logout() {
    this.removeToken()
    this.removeUser()
  },

  // 强制跳转到登录页（适用于内嵌场景）
  redirectToLogin() {
    this.logout()
    // 尝试多种跳转方式，确保在内嵌场景下也能正常工作
    try {
      // 尝试使用React Router的方式
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    } catch (error) {
      console.error('跳转登录页失败:', error)
      // 如果直接跳转失败，尝试其他方式
      try {
        window.parent.location.href = '/login'
      } catch (e) {
        // 最后的备选方案：强制刷新页面
        window.location.reload()
      }
    }
  }
}

