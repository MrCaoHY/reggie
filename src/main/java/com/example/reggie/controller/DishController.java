package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("分页获取菜品信息")
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.hasText(name),Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,dishLambdaQueryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> collect = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            dishDto.setCategoryName(byId.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(collect);
        return R.success(dishDtoPage);
    }

    @ApiOperation("获取该分类的菜品,可售状态")
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        Object cacheDishDto = redisTemplate.opsForValue().get(key);
        //如果缓存有就直接取，没有就查完再存到缓存
        if (cacheDishDto != null) {
            return R.success((List<DishDto>)cacheDishDto);
        }

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);

        List<DishDto> collect = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key,collect);
        return R.success(collect);
    }

    @ApiOperation("根据id获取菜品信息")
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable("id") long id){
        DishDto withFlavorById = dishService.getWithFlavorById(id);
        return R.success(withFlavorById);
    }

    @ApiOperation("新增菜品")
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("新增菜品{}",dishDto);
        dishService.saveWithFlavor(dishDto);
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        return R.success("新增菜品成功");
    }
    @ApiOperation("修改菜品信息")
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("修改菜品信息{}",dishDto);
        dishService.updateWithFlavor(dishDto);
        //清理某个分类下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("修改菜品成功");
    }

    @ApiOperation("修改菜品状态")
    @PostMapping("/status/{state}")
    public R<String> modifyStatus(@PathVariable("state") int state, @RequestParam List<Long> ids){
        log.info("根据id修改菜品的状态:{},id为：{}", state, ids);
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.set(Dish::getStatus,state).in(Dish::getId,ids);
        dishService.update(dishLambdaUpdateWrapper);
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("修改菜品状态成功");
    }

    @ApiOperation("删除菜品信息")
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除菜品信息，ids:{}",ids);
        dishService.removeBatchByIds(Arrays.asList(ids));
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //这里为了省事直接清空
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("删除菜品信息成功");
    }

}
