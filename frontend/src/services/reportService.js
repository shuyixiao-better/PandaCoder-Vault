import request from './request.js'

export const reportService = {
  // 获取我的周报列表（分页）
  getMyReports(page = 0, size = 10) {
    return request.get('/reports/my', {
      params: { page, size }
    })
  },

  // 获取周报详情
  getReportById(id) {
    return request.get(`/reports/${id}`)
  },

  // 按日期范围查询周报
  getReportsByDateRange(startDate, endDate, page = 0, size = 10) {
    return request.get('/reports/range', {
      params: { startDate, endDate, page, size }
    })
  },

  // 删除周报
  deleteReport(id) {
    return request.delete(`/reports/${id}`)
  }
}

