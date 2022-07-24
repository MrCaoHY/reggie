package com.example.reggie.dto;

import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: reggie
 * @description: 新增菜品
 * @author: CaoHaiyang
 * @create: 2022-07-24 22:23
 **/
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
