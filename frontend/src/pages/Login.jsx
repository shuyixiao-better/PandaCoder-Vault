import { useState } from 'react'
import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate, Link } from 'react-router-dom'
import { authService } from '../services/authService.js'
import { auth } from '../utils/auth.js'
import './Login.css'

const Login = () => {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)

  const onFinish = async (values) => {
    setLoading(true)
    try {
      const response = await authService.login(values)
      console.log('登录响应:', response)

      // 后端返回格式: { code: 200, message: "登录成功", data: { token, username, email, ... } }
      if (response.code === 200 && response.data) {
        const { token, ...userData } = response.data
        auth.setToken(token)
        auth.setUser(userData)
        message.success(response.message || '登录成功！')
        navigate('/')
      } else {
        message.error(response.message || '登录失败')
      }
    } catch (error) {
      console.error('登录失败:', error)
      message.error('登录失败，请检查用户名和密码')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-container">
      <Card className="login-card">
        <div className="login-header">
          <div className="logo">
            <span className="logo-icon">🐼</span>
            <span className="logo-text">PandaCoder Vault</span>
          </div>
          <p className="subtitle">程序员的个人知识库</p>
        </div>
        <Form
          name="login"
          onFinish={onFinish}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: '请输入用户名！' }]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="用户名"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: '请输入密码！' }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="密码"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              block
            >
              登录
            </Button>
          </Form.Item>

          <div className="login-footer">
            还没有账号？ <Link to="/register">立即注册</Link>
          </div>
        </Form>
      </Card>
    </div>
  )
}

export default Login

