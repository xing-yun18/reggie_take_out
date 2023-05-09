package com.nuist.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author 浅梦
 * @Date 2023 05 02 13:21
 * 全局异常处理器
 **/
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("服务器繁忙，请重试");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandle(CustomException ex){
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
    @ExceptionHandler(FileNotFoundException.class)
    public R<String> exceptionHandle(FileNotFoundException ex){
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
