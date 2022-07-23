package com.example.reggie.common;

/**
 * @program: reggie
 * @description: 自定义业务异常
 * @author: CaoHaiyang
 * @create: 2022-07-23 23:11
 **/
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
