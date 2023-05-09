package com.nuist.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.reggie.common.R;
import com.nuist.reggie.dto.DishDto;
import com.nuist.reggie.entity.Category;
import com.nuist.reggie.entity.Dish;
import com.nuist.reggie.service.CategoryService;
import com.nuist.reggie.service.DishFlavorService;
import com.nuist.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 浅梦
 * @Date 2023 05 05 22:22
 **/
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询显示菜品信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> list(int page, int pageSize, String name) {
        //设置分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //设置条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //设置模糊查询
        lqw.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //设置排序规则
        lqw.orderByAsc(Dish::getSort);
        //执行查询
        dishService.page(pageInfo, lqw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            //对每一个Dish进行处理
            //将其拷贝到一个dishDto对象里面
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            //获取分类id
            Long categoryId = item.getCategoryId();
            //按id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        //返回结果
        return R.success(dishDtoPage);
    }

    /**
     * 保存菜品数据
     * 包含了菜品口味信息
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        if (dishService.saveWithFlavor(dishDto)) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    /**
     * 按id查询单个菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.selectWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据分类id查询相关菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Long categoryId){
        //构造条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //设置等值查询条件
        lqw.eq(Dish::getCategoryId,categoryId);
        //添加条件，查询状态为1（启售状态的）的菜品
        lqw.eq(Dish::getStatus,1);
        //执行查询
        List<Dish> list = dishService.list(lqw);
        return R.success(list);
    }
}
