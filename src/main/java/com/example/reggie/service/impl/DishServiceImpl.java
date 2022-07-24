package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import com.example.reggie.mapper.DishMapper;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品同时保存口味数据
     * @author caohaiyang
     * @date 2022/7/24 22:32
     * @param dishDto
     * @return boolean
     */
    @Transactional
    @Override
    public boolean saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors().stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return true;
    }

    @Override
    public DishDto getWithFlavorById(long id) {
        DishDto dishDto = new DishDto();
        Dish byId = this.getById(id);
        BeanUtils.copyProperties(byId,dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Transactional
    @Override
    public boolean updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors().stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return true;
    }
}
