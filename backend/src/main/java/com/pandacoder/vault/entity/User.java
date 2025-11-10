package com.pandacoder.vault.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类 - MySQL
 * 存储用户基础信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {

    /**
     * 主键ID - 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编码 - 唯一标识,用于关联MongoDB文档
     */
    @TableField("user_code")
    private String userCode;

    /**
     * 设备唯一标识（基于MAC地址的SHA-256哈希值）
     * 用于区分不同设备的用户，查询周报数据时使用
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 用户名（唯一）
     */
    @TableField("username")
    private String username;

    /**
     * 邮箱（唯一）
     */
    @TableField("email")
    private String email;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 角色（逗号分隔，如：USER,ADMIN）
     */
    @TableField("roles")
    private String roles;

    /**
     * 账号状态：1-启用，0-禁用
     */
    @TableField("enabled")
    @Builder.Default
    private Boolean enabled = true;

    /**
     * 逻辑删除：1-已删除，0-未删除
     */
    @TableLogic
    @TableField("deleted")
    @Builder.Default
    private Integer deleted = 0;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 最后登录时间
     */
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;
}

