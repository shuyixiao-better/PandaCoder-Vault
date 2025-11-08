import { useState, useEffect } from 'react'
import { Card, Table, Button, Space, Tag, message, Popconfirm, Empty } from 'antd'
import { EyeOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import dayjs from 'dayjs'
import { reportService } from '../services/reportService.js'
import './WeeklyReports.css'

const WeeklyReports = () => {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [reports, setReports] = useState([])
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0
  })

  const fetchReports = async (page = 0, size = 10) => {
    setLoading(true)
    try {
      const response = await reportService.getMyReports(page, size)
      if (response.success) {
        const { content, totalElements, number, size: pageSize } = response.data
        setReports(content)
        setPagination({
          current: number + 1,
          pageSize,
          total: totalElements
        })
      }
    } catch (error) {
      console.error('获取周报失败:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchReports()
  }, [])

  const handleTableChange = (newPagination) => {
    fetchReports(newPagination.current - 1, newPagination.pageSize)
  }

  const handleView = (record) => {
    navigate(`/reports/${record.id}`)
  }

  const handleDelete = async (record) => {
    try {
      const response = await reportService.deleteReport(record.id)
      if (response.success) {
        message.success('删除成功')
        fetchReports(pagination.current - 1, pagination.pageSize)
      }
    } catch (error) {
      console.error('删除失败:', error)
    }
  }

  const columns = [
    {
      title: '项目名称',
      dataIndex: 'projectName',
      key: 'projectName',
      width: 200,
      render: (text) => <strong>{text || '未命名项目'}</strong>
    },
    {
      title: '周期',
      key: 'period',
      width: 250,
      render: (_, record) => (
        <span>
          {dayjs(record.weekStartDate).format('YYYY-MM-DD')} ~ {dayjs(record.weekEndDate).format('YYYY-MM-DD')}
        </span>
      )
    },
    {
      title: '提交数',
      dataIndex: 'totalCommits',
      key: 'totalCommits',
      width: 100,
      align: 'center',
      render: (count) => <Tag color="blue">{count}</Tag>
    },
    {
      title: '贡献者',
      dataIndex: 'totalAuthors',
      key: 'totalAuthors',
      width: 100,
      align: 'center',
      render: (count) => <Tag color="green">{count}</Tag>
    },
    {
      title: 'AI 模型',
      dataIndex: 'aiModel',
      key: 'aiModel',
      width: 150,
      render: (model) => model || '-'
    },
    {
      title: '生成时间',
      dataIndex: 'generatedTime',
      key: 'generatedTime',
      width: 180,
      render: (time) => dayjs(time).format('YYYY-MM-DD HH:mm:ss')
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      fixed: 'right',
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            icon={<EyeOutlined />}
            onClick={() => handleView(record)}
          >
            查看
          </Button>
          <Popconfirm
            title="确定要删除这条周报吗？"
            onConfirm={() => handleDelete(record)}
            okText="确定"
            cancelText="取消"
          >
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
            >
              删除
            </Button>
          </Popconfirm>
        </Space>
      )
    }
  ]

  return (
    <div className="weekly-reports">
      <Card
        title="Git 周报列表"
        extra={
          <Button
            icon={<ReloadOutlined />}
            onClick={() => fetchReports(pagination.current - 1, pagination.pageSize)}
          >
            刷新
          </Button>
        }
      >
        <Table
          columns={columns}
          dataSource={reports}
          rowKey="id"
          loading={loading}
          pagination={pagination}
          onChange={handleTableChange}
          scroll={{ x: 1200 }}
          locale={{
            emptyText: (
              <Empty
                description="暂无周报数据"
                image={Empty.PRESENTED_IMAGE_SIMPLE}
              >
                <p>请在 IDEA 插件中生成并归档周报</p>
              </Empty>
            )
          }}
        />
      </Card>
    </div>
  )
}

export default WeeklyReports

