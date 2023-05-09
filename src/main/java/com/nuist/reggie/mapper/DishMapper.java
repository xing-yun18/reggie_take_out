package com.nuist.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuist.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 浅梦
 * @Date 2023 05 05 14:50
 **/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
