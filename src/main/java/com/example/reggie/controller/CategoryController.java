package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.entity.Category;
import com.example.reggie.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: reggie
 * @description: 分类管理
 * @author: CaoHaiyang
 * @create: 2022-07-23 19:59
 **/
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("分页查询分类信息")
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("分页查询分类信息page:{},pageSize{}", page, pageSize);
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage, categoryLambdaQueryWrapper);
        return R.success(categoryPage);
    }

    @ApiOperation("根据id删除分类信息")
    @DeleteMapping
    public R<String> deleteCategoryById(Long id) {
        log.info("删除id为{}的分类信息", id);
        categoryService.remove(id);
        return R.success("删除成功");
    }

    @ApiOperation("根据id获取分类信息")
    @GetMapping("/{id}")
    public R<Category> getCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.getById(id);
        log.info("获取id为{}的分类信息", category);
        return R.success(category);
    }

    @ApiOperation("添加分类信息")
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        log.info("添加分类信息{}",category);
        return R.success("保存成功");
    }

    @ApiOperation("添加分类信息")
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        log.info("修改分类信息{}",category);
        return R.success("修改成功");
    }

}
