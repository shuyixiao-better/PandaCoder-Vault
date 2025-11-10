import { useState, useEffect } from 'react'
import { Card, Descriptions, Tag, Button, Spin, Empty, Space, Typography, Divider } from 'antd'
import { ArrowLeftOutlined, CalendarOutlined, UserOutlined, FileTextOutlined, BranchesOutlined, CodeOutlined } from '@ant-design/icons'
import { useParams, useNavigate } from 'react-router-dom'
import dayjs from 'dayjs'
import { reportService } from '../services/reportService.js'
import './ReportDetail.css'

const { Paragraph, Title, Text } = Typography

const ReportDetail = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [report, setReport] = useState(null)

  useEffect(() => {
    fetchReportDetail()
  }, [id])

  const fetchReportDetail = async () => {
    setLoading(true)
    try {
      const response = await reportService.getReportById(id)
      console.log('获取周报详情响应:', response)

      // 后端返回格式: { code: 200, message: "...", data: {...} }
      if (response.code === 200 && response.data) {
        setReport(response.data)
      }
    } catch (error) {
      console.error('获取周报详情失败:', error)
    } finally {
      setLoading(false)
    }
  }

  // 渲染 Markdown 格式的内容
  const renderMarkdownContent = (content) => {
    if (!content) return null

    // 简单的 Markdown 渲染（支持基本格式）
    const lines = content.split('\n')
    const elements = []
    let currentSection = []

    lines.forEach((line, index) => {
      // 标题处理
      if (line.startsWith('###')) {
        if (currentSection.length > 0) {
          elements.push(<Paragraph key={`p-${index}`}>{currentSection.join('\n')}</Paragraph>)
          currentSection = []
        }
        elements.push(
          <Title level={4} key={`h3-${index}`} style={{ marginTop: 24, color: '#1890ff' }}>
            {line.replace(/^###\s*/, '')}
          </Title>
        )
      } else if (line.startsWith('##')) {
        if (currentSection.length > 0) {
          elements.push(<Paragraph key={`p-${index}`}>{currentSection.join('\n')}</Paragraph>)
          currentSection = []
        }
        elements.push(
          <Title level={3} key={`h2-${index}`} style={{ marginTop: 24, color: '#1890ff' }}>
            {line.replace(/^##\s*/, '')}
          </Title>
        )
      } else if (line.startsWith('#')) {
        if (currentSection.length > 0) {
          elements.push(<Paragraph key={`p-${index}`}>{currentSection.join('\n')}</Paragraph>)
          currentSection = []
        }
        elements.push(
          <Title level={2} key={`h1-${index}`} style={{ marginTop: 24 }}>
            {line.replace(/^#\s*/, '')}
          </Title>
        )
      } else if (line.trim() === '---') {
        if (currentSection.length > 0) {
          elements.push(<Paragraph key={`p-${index}`}>{currentSection.join('\n')}</Paragraph>)
          currentSection = []
        }
        elements.push(<Divider key={`divider-${index}`} />)
      } else if (line.trim().startsWith('|')) {
        // 表格行 - 保持原样显示
        currentSection.push(line)
      } else if (line.trim().startsWith('-') || line.trim().startsWith('*')) {
        // 列表项
        currentSection.push(line)
      } else {
        currentSection.push(line)
      }
    })

    if (currentSection.length > 0) {
      elements.push(
        <Paragraph key="final" style={{ whiteSpace: 'pre-wrap' }}>
          {currentSection.join('\n')}
        </Paragraph>
      )
    }

    return elements
  }

  // 渲染提交记录
  const renderCommits = (commitsText) => {
    if (!commitsText || typeof commitsText !== 'string') return null

    return (
      <div style={{
        backgroundColor: '#f5f5f5',
        padding: '16px',
        borderRadius: '4px',
        fontFamily: 'Monaco, Consolas, "Courier New", monospace',
        fontSize: '13px',
        lineHeight: '1.6',
        whiteSpace: 'pre-wrap',
        wordWrap: 'break-word',
        overflowX: 'auto'
      }}>
        {commitsText}
      </div>
    )
  }

  if (loading) {
    return (
      <div className="report-detail-loading">
        <Spin size="large" />
      </div>
    )
  }

  if (!report) {
    return (
      <Card>
        <Empty description="周报不存在" />
        <div style={{ textAlign: 'center', marginTop: 16 }}>
          <Button onClick={() => navigate('/reports')}>返回列表</Button>
        </div>
      </Card>
    )
  }

  return (
    <div className="report-detail">
      <Card
        title={
          <Space>
            <Button
              icon={<ArrowLeftOutlined />}
              onClick={() => navigate('/reports')}
            >
              返回
            </Button>
            <FileTextOutlined />
            <span>周报详情</span>
          </Space>
        }
      >
        {/* 基本信息 */}
        <Descriptions bordered column={2} size="middle">
          <Descriptions.Item label="项目名称" span={2}>
            <Text strong style={{ fontSize: 16 }}>
              {report.projectName || '未命名项目'}
            </Text>
          </Descriptions.Item>
          <Descriptions.Item label="周期开始" icon={<CalendarOutlined />}>
            <CalendarOutlined style={{ marginRight: 8 }} />
            {dayjs(report.weekStartDate).format('YYYY-MM-DD')}
          </Descriptions.Item>
          <Descriptions.Item label="周期结束">
            <CalendarOutlined style={{ marginRight: 8 }} />
            {dayjs(report.weekEndDate).format('YYYY-MM-DD')}
          </Descriptions.Item>
          <Descriptions.Item label="总提交数">
            <Tag color="blue" style={{ fontSize: 14, padding: '4px 12px' }}>
              <BranchesOutlined /> {report.totalCommits} 次提交
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="贡献者数">
            <Tag color="green" style={{ fontSize: 14, padding: '4px 12px' }}>
              <UserOutlined /> {report.totalAuthors || 0} 位贡献者
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="AI 模型">
            <Text code>{report.aiModel || '-'}</Text>
          </Descriptions.Item>
          <Descriptions.Item label="生成时间">
            {dayjs(report.generatedTime).format('YYYY-MM-DD HH:mm:ss')}
          </Descriptions.Item>
          {report.authorFilter && (
            <Descriptions.Item label="作者筛选" span={2}>
              <Tag color="purple">{report.authorFilter}</Tag>
            </Descriptions.Item>
          )}
        </Descriptions>

        {/* 周报内容 - Markdown 渲染 */}
        <Card
          title={
            <Space>
              <FileTextOutlined />
              <span>周报内容</span>
            </Space>
          }
          style={{ marginTop: 24 }}
          type="inner"
          bodyStyle={{ padding: '24px' }}
        >
          <div style={{
            backgroundColor: '#fff',
            lineHeight: '1.8',
            fontSize: '14px'
          }}>
            {renderMarkdownContent(report.reportContent)}
          </div>
        </Card>

        {/* 提交记录 - 格式化文本显示 */}
        {report.commits && (
          <Card
            title={
              <Space>
                <CodeOutlined />
                <span>Git 提交记录</span>
              </Space>
            }
            style={{ marginTop: 24 }}
            type="inner"
          >
            {renderCommits(report.commits)}
          </Card>
        )}

        {/* 元数据（如果有） */}
        {report.metadata && Object.keys(report.metadata).length > 0 && (
          <Card title="元数据" style={{ marginTop: 24 }} type="inner">
            <pre style={{
              backgroundColor: '#f5f5f5',
              padding: '16px',
              borderRadius: '4px',
              overflow: 'auto'
            }}>
              {JSON.stringify(report.metadata, null, 2)}
            </pre>
          </Card>
        )}
      </Card>
    </div>
  )
}

export default ReportDetail

