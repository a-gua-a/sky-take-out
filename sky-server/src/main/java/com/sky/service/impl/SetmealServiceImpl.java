package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void insert(SetmealDTO setmealDTO) {
        // 转换为实体类
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 新增套餐
        setmealMapper.insert(setmeal);
        //更新关联的菜品
        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        dishList.forEach(dish -> {
            dish.setSetmealId(setmeal.getId());
            setmealDishMapper.insert(dish);
        });
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 删除套餐
        setmealMapper.deleteBatch(ids);
        // 删除套餐菜品
        ids.forEach(id -> {
            setmealDishMapper.deleteBySetmealId(id);
        });
    }

    /**
     * 更新套餐状态
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 更新套餐状态
        Setmeal setmeal = setmealMapper.getById(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    /**
     * 更新套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        // 转换为实体类
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新套餐
        setmealMapper.update(setmeal);
        // 删除原有的关联菜品
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        // 更新关联的菜品
        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        dishList.forEach(dish -> {
            setmealDishMapper.insert(dish);
        });
    }

    /**
     * 分页查询套餐
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealPageQueryDTO, setmeal);
        // 分页查询套餐
        Page<Setmeal> setmealList = setmealMapper.pageQuery(setmeal);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(setmealList.getTotal());
        pageResult.setRecords(setmealList.getResult());
        return pageResult;
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
