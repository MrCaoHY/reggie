package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.entity.Dish;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: reggie
 * @description: 菜品管理
 * @author: CaoHaiyang
 * @create: 2022-07-23 23:38
 **/

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @ApiOperation("分页获取菜品信息")
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.hasText(name),Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,dishLambdaQueryWrapper);
        return R.success(dishPage);
    }

    @ApiOperation("获取该分类的菜品")
    @GetMapping("/list")
    public R<List<Dish>> list(long categoryId) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        return R.success(list);
    }

    @GetMapping("/{id}")
    public R<Dish> getDishById(@PathVariable("id") long id){
        Dish dish = dishService.getById(id);
        return R.success(dish);
    }

    @PostMapping
    public R<String> save(@RequestBody Dish dish){
        dishService.save(dish);
        return R.success("保存成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Dish dish){
        dishService.updateById(dish);
        return R.success("修改成功");
    }
}
