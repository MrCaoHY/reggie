package com.example.reggie.mapper;

import com.example.reggie.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品及套餐分类 Mapper 接口
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
