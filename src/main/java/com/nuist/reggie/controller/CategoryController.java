package com.nuist.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuist.reggie.common.R;
import com.nuist.reggie.entity.Category;
import com.nuist.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 浅梦
 * @Date 2023 05 05 13:24
 **/
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 保存分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("添加分类，分类信息：{}", category.toString());
        if (categoryService.save(category)) {
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        //构建分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        if (categoryService.updateById(category)) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    /**
     * 删除分类
     * 这里需要先判断该分类下是否还有菜品和套餐
     * 没有才能继续删除
     *
     * @param ids 要删除的分类id
     * @return 是否成功
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除的id为{}", ids);
/*        if (categoryService.removeById(ids)){
            return R.success("删除成功");
        }*/
        if (categoryService.remove(ids)) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //按type查询
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType() != null, Category::getType, category.getType());
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }

}
