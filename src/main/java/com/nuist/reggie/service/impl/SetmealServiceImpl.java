package com.nuist.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuist.reggie.common.CustomException;
import com.nuist.reggie.dto.SetmealDto;
import com.nuist.reggie.entity.Category;
import com.nuist.reggie.entity.Setmeal;
import com.nuist.reggie.entity.SetmealDish;
import com.nuist.reggie.mapper.SetmealMapper;
import com.nuist.reggie.service.CategoryService;
import com.nuist.reggie.service.SetmealDishService;
import com.nuist.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 浅梦
 * @Date 2023 05 05 14:53
 **/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    CategoryService categoryService;
    /**
     * 同时保存setmeal和setmealDish两张表
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存setmeal
        this.save(setmealDto);
        Long id = setmealDto.getId();
        //保存setmealDish
        //保存前先设置setmealId和当前setmeal绑定
        List<SetmealDish> list = setmealDto.getSetmealDishes().stream()
                        .peek(item -> item.setSetmealId(id))
                        .collect(Collectors.toList());
        setmealDto.setSetmealDishes(list);
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    /**
     * 按id查询一个dto对象
     * @param id
     * @return
     */
    @Override
    public SetmealDto getWithDish(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        //按id查询两个表
        Setmeal setmeal = this.getById(id);
        //获取到分类id
        Long categoryId = setmeal.getCategoryId();
        //查询分类名称
        Category category = categoryService.getById(categoryId);
        //拷贝对象
        BeanUtils.copyProperties(setmeal,setmealDto);
        //设置分类名称
        setmealDto.setCategoryName(category.getName());
        //根据套餐id查询套餐绑定的菜品
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        //设置查询条件
        lqw.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(lqw);
        //设置dto中的SetmealDishes
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    /**
     * 同时修改setmeal和setmealDish两张表
     * @param setmealDto
     */
    @Transactional
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //先保存setmeal这张表
        this.updateById(setmealDto);
        //获取setmealId
        Long id = setmealDto.getId();
        //再操作setmealDish这张表
        //设置setmealDish的setmealId
        List<SetmealDish> list = setmealDto.getSetmealDishes().stream()
                .peek(item -> item.setSetmealId(id)).collect(Collectors.toList());
        //先删除setmealDish里面的和这个id关联的字段，再重新插入新字段
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(lqw);
        //插入新的list
        setmealDishService.saveBatch(list);
    }

    /**
     * 按id删除两张表的内容
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        for (Long id : ids) {
            //id是要删除的套餐id，先判断这个套餐是否可以删除
            Setmeal setmeal = this.getById(id);
            Integer status = setmeal.getStatus();
            if (status == 1){
                //此时是启售状态
                throw new CustomException("有套餐在启售状态，删除失败");
            }
            //先删除关联的菜品
            LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
            //根据setmealId删除
            lqw.eq(SetmealDish::getSetmealId,id);
            setmealDishService.remove(lqw);
            //再删除套餐
            this.removeById(id);
        }
    }

    /**
     * 实现启售，停售功能
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(List<Long> ids, int status) {
        //先按id找到每一个setmeal
        for (Long id : ids) {
            Setmeal setmeal = this.getById(id);
            //如果status为1
            //把statues全部改成0
            setmeal.setStatus(status == 1 ? 1 : 0);
            updateById(setmeal);
        }
    }
}
