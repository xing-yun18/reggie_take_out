package com.nuist.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuist.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 浅梦
 * @Date 2023 04 25 15:24
 **/

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
