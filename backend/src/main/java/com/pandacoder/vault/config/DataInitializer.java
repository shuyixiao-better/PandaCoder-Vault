package com.pandacoder.vault.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pandacoder.vault.entity.User;
import com.pandacoder.vault.mapper.UserMapper;
import com.pandacoder.vault.util.RoleUtil;
import com.pandacoder.vault.util.UserCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据初始化器
 * 在应用启动时自动创建默认管理员账号
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdminUser();
    }

    /**
     * 初始化管理员账号
     */
    private void initAdminUser() {
        String adminUsername = "admin";

        // 检查管理员账号是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, adminUsername)
        );
        if (count > 0) {
            log.info("管理员账号已存在，跳过初始化");
            return;
        }

        // 创建管理员角色
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        // 创建管理员用户
        User admin = User.builder()
                .userCode(UserCodeGenerator.generate())
                .username(adminUsername)
                .email("admin@pandacoder.com")
                .password(passwordEncoder.encode("admin123"))
                .nickname("管理员")
                .roles(RoleUtil.rolesToString(roles))
                .enabled(true)
                .build();

        userMapper.insert(admin);

        log.info("========================================");
        log.info("✅ 默认管理员账号创建成功！");
        log.info("用户名: admin");
        log.info("密码: admin123");
        log.info("用户编码: {}", admin.getUserCode());
        log.info("⚠️  请在首次登录后立即修改密码！");
        log.info("========================================");
    }
}

