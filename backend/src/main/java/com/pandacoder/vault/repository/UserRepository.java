package com.pandacoder.vault.repository;

import com.pandacoder.vault.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否存在
     */
    Boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    Boolean existsByEmail(String email);
}

