package com.nuist.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.reggie.entity.Employee;
import com.nuist.reggie.mapper.EmployeeMapper;
import com.nuist.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author 浅梦
 * @Date 2023 04 25 15:26
 **/

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
