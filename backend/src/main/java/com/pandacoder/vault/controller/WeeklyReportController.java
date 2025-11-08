package com.pandacoder.vault.controller;

import com.pandacoder.vault.dto.ApiResponse;
import com.pandacoder.vault.model.WeeklyReport;
import com.pandacoder.vault.service.WeeklyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 周报控制器
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    /**
     * 获取我的周报列表（分页）
     */
    @GetMapping("/my")
    public ApiResponse<Page<WeeklyReport>> getMyReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<WeeklyReport> reports = weeklyReportService.getMyReports(page, size);
            return ApiResponse.success(reports);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取周报详情
     */
    @GetMapping("/{id}")
    public ApiResponse<WeeklyReport> getReportById(@PathVariable String id) {
        try {
            WeeklyReport report = weeklyReportService.getReportById(id);
            return ApiResponse.success(report);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据日期范围查询周报
     */
    @GetMapping("/range")
    public ApiResponse<List<WeeklyReport>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<WeeklyReport> reports = weeklyReportService.getReportsByDateRange(startDate, endDate);
            return ApiResponse.success(reports);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除周报
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReport(@PathVariable String id) {
        try {
            weeklyReportService.deleteReport(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

