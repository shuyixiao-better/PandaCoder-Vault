package com.pandacoder.vault.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pandacoder.vault.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus 已提供基础 CRUD 方法
    // 如需自定义 SQL，可在此添加方法并在 XML 中实现
}

