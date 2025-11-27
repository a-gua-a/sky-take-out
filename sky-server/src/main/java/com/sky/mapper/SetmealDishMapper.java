package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishMapper {
        /**
         * 新增套餐菜品
         */
        void insert(SetmealDish setmealDish);


        /**
         * 根据套餐id删除套餐菜品
         */
        void deleteBySetmealId(Long id);
}
