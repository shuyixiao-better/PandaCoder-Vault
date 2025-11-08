import { Layout as AntLayout, Menu, Avatar, Dropdown, Space } from 'antd'
import {
  FileTextOutlined,
  BookOutlined,
  CodeOutlined,
  StarOutlined,
  UserOutlined,
  LogoutOutlined,
  SettingOutlined
} from '@ant-design/icons'
import { useNavigate, useLocation, Outlet } from 'react-router-dom'
import { auth } from '../utils/auth.js'
import './Layout.css'

const { Header, Sider, Content } = AntLayout

const AppLayout = () => {
  const navigate = useNavigate()
  const location = useLocation()
  const user = auth.getUser()

  const menuItems = [
    {
      key: '/reports',
      icon: <FileTextOutlined />,
      label: 'Git 周报'
    },
    {
      key: '/notes',
      icon: <BookOutlined />,
      label: '知识笔记',
      disabled: true
    },
    {
      key: '/snippets',
      icon: <CodeOutlined />,
      label: '代码片段',
      disabled: true
    },
    {
      key: '/bookmarks',
      icon: <StarOutlined />,
      label: '资源收藏',
      disabled: true
    }
  ]

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人信息'
    },
    {
      key: 'settings',
      icon: <SettingOutlined />,
      label: '设置'
    },
    {
      type: 'divider'
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      danger: true
    }
  ]

  const handleMenuClick = ({ key }) => {
    navigate(key)
  }

  const handleUserMenuClick = ({ key }) => {
    if (key === 'logout') {
      auth.logout()
      navigate('/login')
    }
  }

  return (
    <AntLayout className="app-layout">
      <Header className="app-header">
        <div className="logo">
          <span className="logo-icon">🐼</span>
          <span className="logo-text">PandaCoder Vault</span>
        </div>
        <div className="header-right">
          <Dropdown
            menu={{
              items: userMenuItems,
              onClick: handleUserMenuClick
            }}
            placement="bottomRight"
          >
            <Space className="user-info">
              <Avatar icon={<UserOutlined />} src={user?.avatar} />
              <span>{user?.nickname || user?.username}</span>
            </Space>
          </Dropdown>
        </div>
      </Header>
      <AntLayout>
        <Sider width={200} className="app-sider">
          <Menu
            mode="inline"
            selectedKeys={[location.pathname]}
            items={menuItems}
            onClick={handleMenuClick}
          />
        </Sider>
        <Content className="app-content">
          <Outlet />
        </Content>
      </AntLayout>
    </AntLayout>
  )
}

export default AppLayout

