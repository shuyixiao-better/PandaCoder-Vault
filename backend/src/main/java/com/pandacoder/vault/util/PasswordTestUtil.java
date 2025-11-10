package com.pandacoder.vault.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码测试工具
 * 用于生成和验证BCrypt密码
 */
public class PasswordTestUtil {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 测试密码
        String rawPassword = "admin123";
        String encodedPassword = "$2a$10$AXbfMA2G7g9nxOw5hnYaaehMTGhbJi1UjC4CQbUt2g8w6xUvQD4QG";
        
        // 生成新的加密密码
        String newEncodedPassword = encoder.encode(rawPassword);
        
        System.out.println("========================================");
        System.out.println("原始密码: " + rawPassword);
        System.out.println("数据库中的密码: " + encodedPassword);
        System.out.println("新生成的密码: " + newEncodedPassword);
        System.out.println("========================================");
        
        // 验证密码是否匹配
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证结果: " + (matches ? "✅ 匹配" : "❌ 不匹配"));
        
        if (!matches) {
            System.out.println("\n⚠️  数据库中的密码不正确！");
            System.out.println("请执行以下SQL更新密码：");
            System.out.println("UPDATE users SET password = '" + newEncodedPassword + "' WHERE username = 'admin';");
        }
        
        System.out.println("========================================");
    }
}

