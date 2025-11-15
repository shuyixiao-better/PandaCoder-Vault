import { Navigate } from 'react-router-dom'
import { auth } from '../utils/auth.js'
import { tokenChecker } from '../utils/tokenChecker.js'

const PrivateRoute = ({ children }) => {
  // 检查token是否存在和是否过期
  const isValidToken = auth.isAuthenticated()
  
  // 如果token无效，跳转到登录页
  if (!isValidToken) {
    console.log('Token无效或已过期，重定向到登录页')
    auth.redirectToLogin()
    return null
  }
  
  // 额外检查一次token是否即将过期
  const remainingTime = auth.getTokenRemainingTime()
  if (remainingTime > 0 && remainingTime < 60 * 1000) { // 剩余时间少于1分钟
    console.warn('Token即将过期，剩余时间:', Math.round(remainingTime / 1000), '秒')
  }
  
  return children
}

export default PrivateRoute

