package com.pandacoder.vault.config;

import com.pandacoder.vault.model.User;
import com.pandacoder.vault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    private final UserRepository userRepository;
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
        if (userRepository.existsByUsername(adminUsername)) {
            log.info("管理员账号已存在，跳过初始化");
            return;
        }

        // 创建管理员角色
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        // 创建管理员用户
        User admin = User.builder()
                .username(adminUsername)
                .email("admin@pandacoder.com")
                .password(passwordEncoder.encode("admin123"))
                .nickname("管理员")
                .roles(roles)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        
        log.info("========================================");
        log.info("✅ 默认管理员账号创建成功！");
        log.info("用户名: admin");
        log.info("密码: admin123");
        log.info("⚠️  请在首次登录后立即修改密码！");
        log.info("========================================");
    }
}

