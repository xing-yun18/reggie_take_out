package com.nuist.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.reggie.dto.SetmealDto;
import com.nuist.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Author 浅梦
 * @Date 2023 05 05 14:51
 **/
public interface SetmealService extends IService<Setmeal> {
    //保存setmeal和setmealDish
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
    //删除数据
    void deleteWithDish(List<Long> ids);

    void updateStatus(List<Long> ids, int status);
}
