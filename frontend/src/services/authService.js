import request from './request.js'

export const authService = {
  // 用户注册
  register(data) {
    return request.post('/auth/register', data)
  },

  // 用户登录
  login(data) {
    return request.post('/auth/login', data)
  },

  // 测试 API
  test() {
    return request.get('/auth/test')
  }
}

