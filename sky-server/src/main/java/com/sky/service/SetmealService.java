package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetmealService {
        /**
         * 新增套餐
         * @param setmealDTO
         */
        void insert(SetmealDTO setmealDTO);


        /**
         * 批量删除套餐
         */
        void deleteBatch(List<Long> ids);

        /**
         * 套餐起售/停售
         */
        void updateStatus(Integer status, Long id);

        /**
         * 更新套餐
         */
        void update(SetmealDTO setmealDTO);

        /**
         * 分页查询套餐
         */
        PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

        /**
        * 条件查询
        * @param setmeal
        * @return
         */
        List<Setmeal> list(Setmeal setmeal);

        /**
        * 根据id查询菜品选项
        * @param id
         * @return
         */
        List<DishItemVO> getDishItemById(Long id);
}
