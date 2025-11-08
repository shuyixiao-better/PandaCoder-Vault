package com.pandacoder.vault.service;

import com.pandacoder.vault.model.WeeklyReport;
import com.pandacoder.vault.repository.WeeklyReportRepository;
import com.pandacoder.vault.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 周报服务
 */
@Service
@RequiredArgsConstructor
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    /**
     * 获取当前登录用户ID
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getId();
        }
        throw new RuntimeException("未登录或登录已过期");
    }

    /**
     * 获取当前用户的周报列表（分页）
     */
    public Page<WeeklyReport> getMyReports(int page, int size) {
        String userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedTime"));
        return weeklyReportRepository.findByUserId(userId, pageable);
    }

    /**
     * 根据ID获取周报详情
     */
    public WeeklyReport getReportById(String id) {
        WeeklyReport report = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("周报不存在"));

        // 验证权限：只能查看自己的周报
        String userId = getCurrentUserId();
        if (report.getUserId() != null && !report.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问此周报");
        }

        return report;
    }

    /**
     * 根据日期范围查询周报
     */
    public List<WeeklyReport> getReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        String userId = getCurrentUserId();
        return weeklyReportRepository.findByUserIdAndWeekStartDateBetween(userId, startDate, endDate);
    }

    /**
     * 删除周报
     */
    public void deleteReport(String id) {
        WeeklyReport report = getReportById(id);
        weeklyReportRepository.delete(report);
    }

    /**
     * 获取所有未关联用户的周报（用于数据迁移）
     */
    public List<WeeklyReport> getUnassignedReports() {
        return weeklyReportRepository.findByUserIdIsNull();
    }

    /**
     * 关联周报到用户（用于数据迁移）
     */
    public void assignReportToUser(String reportId, String userId) {
        WeeklyReport report = weeklyReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("周报不存在"));
        report.setUserId(userId);
        weeklyReportRepository.save(report);
    }
}

