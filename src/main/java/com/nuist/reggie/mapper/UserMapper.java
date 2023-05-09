package com.nuist.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuist.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 浅梦
 * @Date 2023 05 08 20:07
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
