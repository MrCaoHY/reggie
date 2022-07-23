package com.example.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @program: reggie
 * @description: 全局异常处理
 * @author: CaoHaiyang
 * @create: 2022-07-22 20:58
 **/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @author caohaiyang
     * @date 2022/7/22 21:04
     * @return com.example.reggie.common.R<java.lang.String>
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String s1 = s[2] + "已存在";
            return R.error(s1);
        }
        return R.error("未知错误");
    }

    /**
     * 自定义异常处理方法
     * @author caohaiyang
     * @date 2022/7/22 21:04
     * @return com.example.reggie.common.R<java.lang.String>
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String s1 = s[2] + "已存在";
            return R.error(s1);
        }
        return R.error(ex.getMessage());
    }
}
