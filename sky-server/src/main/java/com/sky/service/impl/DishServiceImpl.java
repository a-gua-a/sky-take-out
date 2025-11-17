package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    private DishFlavorMapper dishFlavorMapper;
    private CategoryMapper categoryMapper;


    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 新增菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (CollectionUtils.isNotEmpty(flavors)) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 更新菜品
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        // 更新菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        // 先删除原有的口味
        dishFlavorMapper.deleteByDishId(dish.getId());
        // 新增菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (CollectionUtils.isNotEmpty(flavors)) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }


    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        // 查询菜品
        Dish dish = dishMapper.selectById(id);
        // 查询菜品口味
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        // 查询分类
        Category category = categoryMapper.selectById(dish.getCategoryId());
        // 封装VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        dishVO.setCategoryName(category.getName());
        return dishVO;
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 删除菜品
        dishMapper.deleteBatch(ids);
        // 删除菜品口味
        ids.forEach(id -> {
            dishFlavorMapper.deleteByDishId(id);
        });
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        // 查询菜品
        Page<DishVO> dishList = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(dishList.getTotal(), dishList.getResult());
    }

    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        // 查询菜品
        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);
        return dishList;
    }


}
