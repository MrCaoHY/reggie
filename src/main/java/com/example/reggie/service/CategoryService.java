package com.example.reggie.service;

import com.example.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
public interface CategoryService extends IService<Category> {
    public boolean remove(Long id);

}
