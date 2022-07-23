package com.example.reggie.common;

/**
 * @program: reggie
 * @description: 基于ThreadLocal封装工具类 保存和获取当前登录用户id 后期用redis就可以
 * @author: CaoHaiyang
 * @create: 2022-07-23 21:29
 **/
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
