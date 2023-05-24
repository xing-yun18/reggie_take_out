package com.nuist.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nuist.reggie.common.R;
import com.nuist.reggie.entity.User;
import com.nuist.reggie.dto.userDto;
import com.nuist.reggie.service.UserService;
import com.nuist.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 实现手机端用户登录功能
     * @return
     */
    @Transactional
    @PostMapping("/login")
    public R<User> login(@RequestBody userDto userDto , HttpSession session){
        String phone = userDto.getPhone();
        String code = userDto.getCode();
        //先查表判断数据库里面是否存在这个手机号
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        //构建查询条件
        lqw.eq(phone!=null,User::getPhone,phone);
        User user = userService.getOne(lqw);
        //从session取出正确的验证码
        String rightCode = (String) session.getAttribute(phone);
        log.info("phone == {}  code  == {}  rightCode == {}",phone,code,rightCode);
        //此时用户存在，直接判断验证码是否正确
        if (rightCode.equals(code)){
            if (user == null){
                //用户不存在，注册
                User newUser = new User();
                newUser.setPhone(phone);
                //保存用户信息
                //userService.save(newUser);
                log.info(newUser.getPhone()+"注册成功");
                user = newUser;
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }else {
           return R.error("验证码错误");
        }
    }
}
