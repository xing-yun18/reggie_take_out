package com.nuist.reggie.dto;

import com.nuist.reggie.entity.User;
import lombok.Data;

/**
 * @Author 浅梦
 * @Date 2023 05 24 15:03
 **/
@Data
public class userDto extends User {
    private String code;
}
