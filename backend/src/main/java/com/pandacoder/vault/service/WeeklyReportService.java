package com.pandacoder.vault.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pandacoder.vault.entity.User;
import com.pandacoder.vault.mapper.UserMapper;
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
    private final UserMapper userMapper;

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
     * 获取当前登录用户的设备ID
     */
    private String getCurrentDeviceId() {
        String userId = getCurrentUserId();
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getId, Long.parseLong(userId))
        );
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user.getDeviceId();
    }

    /**
     * 获取当前用户的周报列表（分页）
     */
    public Page<WeeklyReport> getMyReports(int page, int size) {
        String deviceId = getCurrentDeviceId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedTime"));
        return weeklyReportRepository.findByDeviceId(deviceId, pageable);
    }

    /**
     * 根据ID获取周报详情
     */
    public WeeklyReport getReportById(String id) {
        WeeklyReport report = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("周报不存在"));

        // 验证权限：只能查看自己的周报
        String deviceId = getCurrentDeviceId();
        if (report.getDeviceId() != null && !report.getDeviceId().equals(deviceId)) {
            throw new RuntimeException("无权访问此周报");
        }

        return report;
    }

    /**
     * 根据日期范围查询周报
     */
    public List<WeeklyReport> getReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        String deviceId = getCurrentDeviceId();
        return weeklyReportRepository.findByDeviceIdAndWeekStartDateBetween(deviceId, startDate, endDate);
    }

    /**
     * 删除周报
     */
    public void deleteReport(String id) {
        WeeklyReport report = getReportById(id);
        weeklyReportRepository.delete(report);
    }

    /**
     * 获取所有未关联设备的周报（用于数据迁移）
     */
    public List<WeeklyReport> getUnassignedReports() {
        return weeklyReportRepository.findByDeviceIdIsNull();
    }

    /**
     * 关联周报到设备（用于数据迁移）
     */
    public void assignReportToDevice(String reportId, String deviceId) {
        WeeklyReport report = weeklyReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("周报不存在"));
        report.setDeviceId(deviceId);
        weeklyReportRepository.save(report);
    }
}

