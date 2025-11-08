const TOKEN_KEY = 'pandacoder_token'
const USER_KEY = 'pandacoder_user'

export const auth = {
  // 保存 Token
  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token)
  },

  // 获取 Token
  getToken() {
    return localStorage.getItem(TOKEN_KEY)
  },

  // 删除 Token
  removeToken() {
    localStorage.removeItem(TOKEN_KEY)
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
    return !!this.getToken()
  },

  // 登出
  logout() {
    this.removeToken()
    this.removeUser()
  }
}

