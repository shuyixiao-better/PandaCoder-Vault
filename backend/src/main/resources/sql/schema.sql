-- PandaCoder-Vault 数据库初始化脚本
-- MySQL 版本: 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `PandaCoder` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `PandaCoder`;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                       `user_code` VARCHAR(50) NOT NULL COMMENT '用户编码（唯一标识，用于关联MongoDB文档）',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `roles` VARCHAR(255) DEFAULT 'USER' COMMENT '角色（逗号分隔，如：USER,ADMIN）',
    `enabled` TINYINT(1) DEFAULT 1 COMMENT '账号状态：1-启用，0-禁用',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：1-已删除，0-未删除',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_code` (`user_code`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_enabled` (`enabled`),
    KEY `idx_deleted` (`deleted`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

