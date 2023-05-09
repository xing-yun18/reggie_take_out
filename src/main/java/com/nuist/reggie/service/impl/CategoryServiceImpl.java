package com.nuist.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.reggie.common.CustomException;
import com.nuist.reggie.entity.Category;
import com.nuist.reggie.entity.Dish;
import com.nuist.reggie.entity.Setmeal;
import com.nuist.reggie.mapper.CategoryMapper;
import com.nuist.reggie.service.CategoryService;
import com.nuist.reggie.service.DishService;
import com.nuist.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 浅梦
 * @Date 2023 05 05 13:23
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public boolean remove(Long id) {
        //先查询是否有关联的菜品和套餐，如果没有再删除，有就抛出异常
        //添加查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        //执行查询
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //抛出异常
            throw new CustomException("已关联菜品，删除失败");
        }
        //设置查询条件
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //抛出异常
            throw new CustomException("已关联套餐，删除失败");
        }
        return super.removeById(id);
    }
}
