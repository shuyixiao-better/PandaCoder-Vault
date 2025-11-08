package com.pandacoder.vault.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色工具类
 * 用于处理角色在 String 和 Set<String> 之间的转换
 */
public class RoleUtil {

    private static final String DELIMITER = ",";

    /**
     * 将角色集合转换为字符串（逗号分隔）
     * @param roles 角色集合
     * @return 角色字符串，如 "USER,ADMIN"
     */
    public static String rolesToString(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return String.join(DELIMITER, roles);
    }

    /**
     * 将角色字符串转换为集合
     * @param rolesStr 角色字符串，如 "USER,ADMIN"
     * @return 角色集合
     */
    public static Set<String> stringToRoles(String rolesStr) {
        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(rolesStr.split(DELIMITER))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}

