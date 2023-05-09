package com.nuist.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuist.reggie.entity.Category;

/**
 * @Author 浅梦
 * @Date 2023 05 05 13:22
 **/
public interface CategoryService extends IService<Category> {

     boolean remove(Long id);
}
