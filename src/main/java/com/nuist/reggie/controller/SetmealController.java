package com.nuist.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.reggie.common.R;
import com.nuist.reggie.dto.SetmealDto;
import com.nuist.reggie.entity.Category;
import com.nuist.reggie.entity.Setmeal;
import com.nuist.reggie.service.CategoryService;
import com.nuist.reggie.service.SetmealDishService;
import com.nuist.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 浅梦
 * @Date 2023 05 07 15:51
 **/
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    SetmealService setmealService;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    CategoryService categoryService;
    /**
     * 保存新套餐
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        //此处要同时操作两张表
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page ,int pageSize,String name){
        //构建分页查询器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> pageDto = new Page<>(page,pageSize);
        //构建条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        //设置模糊查询条件
        lqw.like(name!=null,Setmeal::getName,name);
        //设置排序条件
        lqw.orderByDesc(Setmeal::getUpdateTime);
        //查询
        setmealService.page(pageInfo,lqw);

        //对象拷贝,其中records字段是分页查询到的值，因为需要修改，所有先不急着拷贝 等修改完了再拷贝
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        List<SetmealDto> list = pageInfo.getRecords().stream().map(item -> {
            //获取分类id
            Long categoryId = item.getCategoryId();
            //将每个Setmeal拷贝到dto里面
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            //查询分类名
            Category category = categoryService.getById(categoryId);

            //设置分类名称
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(list);
        return R.success(pageDto);
    }

    /**
     * 按id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getWithDish(id);
        return R.success(setmealDto);
    }

    /**
     *同时修改setmeal和setmealDish两张表
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 删除套餐和关联信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 实现套餐的启售和停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,@RequestParam List<Long> ids){
        setmealService.updateStatus(ids,status);
        return R.success("修改成功");
    }
}
