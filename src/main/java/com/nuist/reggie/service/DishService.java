package com.nuist.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.reggie.dto.DishDto;
import com.nuist.reggie.entity.Dish;

/**
 * @Author 浅梦
 * @Date 2023 05 05 14:52
 **/
public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);
    DishDto selectWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
