package com.nuist.reggie.dto;

import com.nuist.reggie.entity.Setmeal;
import com.nuist.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
