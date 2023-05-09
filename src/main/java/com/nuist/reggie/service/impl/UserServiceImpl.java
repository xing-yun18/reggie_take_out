package com.nuist.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.reggie.entity.User;
import com.nuist.reggie.mapper.UserMapper;
import com.nuist.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author 浅梦
 * @Date 2023 05 08 20:08
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
