package com.nuist.reggie.controller;

import com.nuist.reggie.common.R;
import com.nuist.reggie.entity.User;
import com.nuist.reggie.service.UserService;
import com.nuist.reggie.utils.SMSUtils;
import com.nuist.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author 浅梦
 * @Date 2023 05 08 20:19
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if (phone == null){
            return R.error("短信发送失败！");
        }
       //生成随机四位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code = {}",code);
        //调用阿里云的api完成发送短信
        //SMSUtils.sendMessage("","",phone,code);
        //将验证码保存到session上
        session.setAttribute(phone,code);
        return R.success("手机验证码发送成功！");
    }
}
