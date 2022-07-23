package com.example.reggie.config;

import com.example.reggie.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @program: reggie
 * @description: 序列化统一处理
 * @author: CaoHaiyang
 * @create: 2022-07-23 17:15
 **/
@Configuration
public class WebJsonConvertConfig extends WebMvcConfigurationSupport {
    //静态资源映射 继承这个类之后原有的自动配置的静态资源无法访问 需要手动处理


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展mvc框架的消息处理，把自己的json映射器加进去 优先级调整为0
     * @author caohaiyang
     * @date 2022/7/23 17:45
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器追加到mvc框架转换器容器中
        converters.add(0,messageConverter);
    }
}
