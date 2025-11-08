package com.pandacoder.vault.repository;

import com.pandacoder.vault.model.WeeklyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 周报数据访问层
 */
@Repository
public interface WeeklyReportRepository extends MongoRepository<WeeklyReport, String> {

    /**
     * 根据用户编码查找周报（分页）
     */
    Page<WeeklyReport> findByUserCode(String userCode, Pageable pageable);

    /**
     * 根据项目名称查找周报
     */
    List<WeeklyReport> findByProjectName(String projectName);

    /**
     * 根据日期范围查找周报
     */
    List<WeeklyReport> findByWeekStartDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据用户编码和日期范围查找周报
     */
    List<WeeklyReport> findByUserCodeAndWeekStartDateBetween(String userCode, LocalDate startDate, LocalDate endDate);

    /**
     * 查找所有周报（用于初始数据关联）
     */
    List<WeeklyReport> findByUserCodeIsNull();
}

