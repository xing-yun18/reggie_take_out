package com.nuist.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.reggie.common.CustomException;
import com.nuist.reggie.dto.DishDto;
import com.nuist.reggie.entity.Dish;
import com.nuist.reggie.entity.DishFlavor;
import com.nuist.reggie.mapper.DishMapper;
import com.nuist.reggie.service.DishFlavorService;
import com.nuist.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author 浅梦
 * @Date 2023 05 05 14:52
 **/
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 将最基本的菜品数据和口味数据一同保存到数据库
     * @param dishDto
     * @return
     */
    @Override
    @Transactional
    public boolean saveWithFlavor(DishDto dishDto) {
        //先保存基本的菜品数据
        super.save(dishDto);
        Long id = dishDto.getId();
        //保存flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            //给每一个id赋值
            flavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
        return true;
    }

    /**
     * 根据id查询一个DishDto对象
     * @param id
     * @return DishDto对象
     */
    @Override
    public DishDto selectWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        //先查询最基本的Dish信息
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        //再查询DishFlavor信息
        //先获取dishId
        Long dishId = dish.getId();
        //设置条件构造器
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishId);
        //查询到口味信息
        List<DishFlavor> list = dishFlavorService.list(lqw);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 修改Dish表和DishFlavor表
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //先修改dish表
        this.updateById(dishDto);

        //修改dishFlavor表
        //先把dishFlavor表里面和当前食物关联的Flavor全部删除
        //构造条件构造器
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        //删除字段
        dishFlavorService.remove(lqw);
        //根据dishDto里面的Flavor修改dishFlavor表
        //保存flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            //给每一个id赋值
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }
}
