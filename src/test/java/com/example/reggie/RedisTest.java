package com.example.reggie;

import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @program: reggie
 * @description:
 * @author: CaoHaiyang
 * @create: 2022-07-27 16:22
 **/
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void test1(){
        LocalDateTime now = LocalDateTime.now();
        redisTemplate.opsForValue().set("time",now);
    }

    @Test
    void testSet(){
//        Dish dish = new Dish();
        DishDto dishDto = new DishDto();
        dishDto.setCreateTime(LocalDateTime.now());
        DishDto dishDto1 = new DishDto();
        ArrayList<DishDto> dishDtos = new ArrayList<>();
        dishDtos.add(dishDto);
        dishDtos.add(dishDto1);

        redisTemplate.opsForValue().set("dish_1321231_1",dishDtos);
    }
}
