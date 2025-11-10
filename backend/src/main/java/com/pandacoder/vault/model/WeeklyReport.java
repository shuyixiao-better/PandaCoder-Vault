package com.pandacoder.vault.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 周报实体类
 * 对应IDEA插件归档的周报数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "weekly_reports")
public class WeeklyReport {

    /**
     * 周报唯一标识
     */
    @Id
    private String id;

    /**
     * 周报内容（AI生成的周报文本）
     */
    private String reportContent;

    /**
     * Git提交日志（原始数据）
     */
    private String commits;

    /**
     * 生成时间
     */
    private LocalDateTime generatedTime;

    /**
     * 周范围 - 开始日期
     */
    private LocalDate weekStartDate;

    /**
     * 周范围 - 结束日期
     */
    private LocalDate weekEndDate;

    /**
     * 作者筛选条件（null表示全部作者）
     */
    private String authorFilter;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * AI模型信息
     */
    private String aiModel;

    /**
     * API地址
     */
    private String apiUrl;

    /**
     * 提交统计信息
     */
    private Integer totalCommits;

    /**
     * 作者数量
     */
    private Integer totalAuthors;

    /**
     * 扩展字段（用于存储其他自定义信息）
     */
    private Map<String, Object> metadata;

    /**
     * 设备唯一标识（基于MAC地址的SHA-256哈希值）
     * 用于区分不同设备的用户，关联到MySQL用户表的deviceId字段
     * 用于多用户场景，关联用户基础信息
     * 注意：IDEA插件归档的数据可能没有这个字段，需要后续关联
     */
    private String deviceId;
}

