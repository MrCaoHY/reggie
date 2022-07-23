package com.example.reggie;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: reggie
 * @description:
 * @author: CaoHaiyang
 * @create: 2022-07-20 17:05
 **/

@SpringBootTest
public class MysqlGeneratorTest {

    @Test
    void testSimple(){
        List<String> tables = new ArrayList<>();

        tables.add("address_book");
        tables.add("category");
        tables.add("dish");
        tables.add("dish_flavor");
        tables.add("employee");
        tables.add("order_detail");
        tables.add("orders");
        tables.add("setmeal");
        tables.add("setmeal_dish");
        tables.add("shopping_cart");
        tables.add("user");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/reggie", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("chy") // 设置作者
                            .outputDir(System.getProperty("user.dir")+ File.separator+"src/main/java")//输出路径
                            .enableSwagger()
                            .commentDate("yyyy-MM-dd");
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent("com.example") // 设置父包名
                            .moduleName("reggie")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("serviceImpl")
                            .controller("controller")
                            .xml("mapper")// 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,System.getProperty("user.dir")+ File.separator+"src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }

    @Test
    void Test(){
        String property = System.getProperty("user.dir");
        System.out.println(property);
    }
}
