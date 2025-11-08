import { useState, useEffect } from 'react'
import { Card, Descriptions, Tag, Button, Spin, Empty, Timeline, Typography } from 'antd'
import { ArrowLeftOutlined, CalendarOutlined, UserOutlined, CodeOutlined } from '@ant-design/icons'
import { useParams, useNavigate } from 'react-router-dom'
import dayjs from 'dayjs'
import { reportService } from '../services/reportService.js'
import './ReportDetail.css'

const { Paragraph, Title } = Typography

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
            <span>周报详情</span>
          </Space>
        }
      >
        <Descriptions bordered column={2}>
          <Descriptions.Item label="项目名称" span={2}>
            <strong>{report.projectName || '未命名项目'}</strong>
          </Descriptions.Item>
          <Descriptions.Item label="周期开始">
            <CalendarOutlined /> {dayjs(report.weekStartDate).format('YYYY-MM-DD')}
          </Descriptions.Item>
          <Descriptions.Item label="周期结束">
            <CalendarOutlined /> {dayjs(report.weekEndDate).format('YYYY-MM-DD')}
          </Descriptions.Item>
          <Descriptions.Item label="总提交数">
            <Tag color="blue">{report.totalCommits}</Tag>
          </Descriptions.Item>
          <Descriptions.Item label="贡献者数">
            <Tag color="green">{report.totalAuthors}</Tag>
          </Descriptions.Item>
          <Descriptions.Item label="AI 模型">
            {report.aiModel || '-'}
          </Descriptions.Item>
          <Descriptions.Item label="生成时间">
            {dayjs(report.generatedTime).format('YYYY-MM-DD HH:mm:ss')}
          </Descriptions.Item>
        </Descriptions>

        <Card title="周报内容" style={{ marginTop: 24 }} type="inner">
          <Paragraph>
            <pre style={{ whiteSpace: 'pre-wrap', wordWrap: 'break-word' }}>
              {report.reportContent}
            </pre>
          </Paragraph>
        </Card>

        {report.commits && report.commits.length > 0 && (
          <Card title="提交记录" style={{ marginTop: 24 }} type="inner">
            <Timeline
              items={report.commits.map((commit, index) => ({
                key: index,
                children: (
                  <div>
                    <div>
                      <UserOutlined /> <strong>{commit.author}</strong>
                      <span style={{ marginLeft: 16, color: '#999' }}>
                        {dayjs(commit.date).format('YYYY-MM-DD HH:mm:ss')}
                      </span>
                    </div>
                    <div style={{ marginTop: 8 }}>
                      <CodeOutlined /> {commit.message}
                    </div>
                    {commit.hash && (
                      <div style={{ marginTop: 4, color: '#999', fontSize: 12 }}>
                        {commit.hash}
                      </div>
                    )}
                  </div>
                )
              }))}
            />
          </Card>
        )}

        {report.metadata && Object.keys(report.metadata).length > 0 && (
          <Card title="元数据" style={{ marginTop: 24 }} type="inner">
            <pre>{JSON.stringify(report.metadata, null, 2)}</pre>
          </Card>
        )}
      </Card>
    </div>
  )
}

export default ReportDetail

