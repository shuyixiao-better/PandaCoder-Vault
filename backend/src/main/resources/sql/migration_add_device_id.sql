-- 数据库迁移脚本：添加 device_id 字段
-- 执行时间：2025-11-10
-- 说明：为 users 表添加 device_id 字段，用于存储设备唯一标识

USE `PandaCoder`;

-- 添加 device_id 字段
ALTER TABLE `users` 
ADD COLUMN `device_id` VARCHAR(64) DEFAULT NULL COMMENT '设备唯一标识（基于MAC地址的SHA-256哈希值）' 
AFTER `user_code`;

-- 添加索引
ALTER TABLE `users` 
ADD KEY `idx_device_id` (`device_id`);

-- 查看表结构
DESCRIBE `users`;

