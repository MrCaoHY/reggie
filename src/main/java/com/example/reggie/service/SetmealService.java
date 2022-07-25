package com.example.reggie.service;

import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
public interface SetmealService extends IService<Setmeal> {

    boolean saveWithDish(SetmealDto setmealDto);

    boolean updateWithDish(SetmealDto setmealDto);

    boolean removeWithDish(List<Long> ids);
}
