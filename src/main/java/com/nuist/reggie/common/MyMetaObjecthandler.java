package com.nuist.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Author 浅梦
 * @Date 2023 05 02 15:37
 * 自定义元数据处理器
 **/
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    @Autowired
    private HttpServletRequest request;
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",request.getSession().getAttribute("employee"));
        metaObject.setValue("updateUser",request.getSession().getAttribute("employee"));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充[update]");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",request.getSession().getAttribute("employee"));
    }
}
