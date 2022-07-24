package com.example.reggie.service;

import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
public interface DishService extends IService<Dish> {
    //新增菜谱并插入口味数据
    public boolean saveWithFlavor(DishDto dishDto);

    public DishDto getWithFlavorById(long id);

    public boolean updateWithFlavor(DishDto dishDto);
}
