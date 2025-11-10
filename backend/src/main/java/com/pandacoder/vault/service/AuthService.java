package com.pandacoder.vault.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pandacoder.vault.dto.AuthResponse;
import com.pandacoder.vault.dto.LoginRequest;
import com.pandacoder.vault.dto.RegisterRequest;
import com.pandacoder.vault.entity.User;
import com.pandacoder.vault.mapper.UserMapper;
import com.pandacoder.vault.security.UserDetailsImpl;
import com.pandacoder.vault.util.DeviceIdentifierUtil;
import com.pandacoder.vault.util.JwtUtil;
import com.pandacoder.vault.util.RoleUtil;
import com.pandacoder.vault.util.UserCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 认证服务
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        Long usernameCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())
        );
        if (usernameCount > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        Long emailCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getEmail, request.getEmail())
        );
        if (emailCount > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 生成设备唯一标识
        String deviceId = DeviceIdentifierUtil.getDeviceId().length() >= 12 ?
                DeviceIdentifierUtil.getDeviceId().substring(0, 12).toUpperCase() : DeviceIdentifierUtil.getDeviceId().toUpperCase();
        // 验证编码是否存在
        Long deviceIdCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getDeviceId, deviceId)
        );
        if (deviceIdCount > 0) {
            throw new RuntimeException("当前设备已经存在用户不允许重复注册");
        }

        // 创建新用户
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        User user = User.builder()
                .userCode(UserCodeGenerator.generate())
                .deviceId(deviceId)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .roles(RoleUtil.rolesToString(roles))
                .enabled(true)
                .build();

        userMapper.insert(user);

        // 生成JWT token
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .roles(RoleUtil.stringToRoles(user.getRoles()))
                .build();
    }

    /**
     * 用户登录
     */
    public AuthResponse login(LoginRequest request) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT token
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        // 更新最后登录时间和设备ID
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())
        );
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 生成设备唯一标识
        String deviceId = DeviceIdentifierUtil.getDeviceId().length() >= 12 ?
                DeviceIdentifierUtil.getDeviceId().substring(0, 12).toUpperCase() : DeviceIdentifierUtil.getDeviceId().toUpperCase();

        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .eq(User::getId, user.getId())
                .set(User::getLastLoginAt, LocalDateTime.now())
                .set(User::getDeviceId, deviceId)
        );

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .roles(userDetails.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .collect(java.util.stream.Collectors.toSet()))
                .build();
    }
}

