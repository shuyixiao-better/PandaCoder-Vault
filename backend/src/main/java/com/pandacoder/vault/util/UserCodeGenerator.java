package com.pandacoder.vault.util;

import java.util.UUID;

/**
 * 用户编码生成器
 */
public class UserCodeGenerator {

    /**
     * 生成唯一的用户编码
     * 格式: UC + 时间戳后8位 + UUID前8位
     * 示例: UC12345678abcd1234
     */
    public static String generate() {
        long timestamp = System.currentTimeMillis();
        String timestampPart = String.valueOf(timestamp).substring(5); // 取后8位
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "UC" + timestampPart + uuidPart;
    }
}

