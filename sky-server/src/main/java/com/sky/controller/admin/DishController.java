package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    @PostMapping("/save")
    @ApiOperation("新增菜品")
    @CacheEvict(value = "dishCategoryCache", key = "#dishDTO.categoryId")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }


    /**
     * 更新菜品
     *
     * @param dishDTO
     */
    @PostMapping("/update")
    @ApiOperation("更新菜品")
    @CacheEvict(value = {"dishCategoryCache","setmealDishCache"}, allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("更新菜品:{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }


    /**
     * 根据id查询菜品
     *
     * @param id
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 删除菜品
     * @param ids 菜品id列表
     * @return
     */
    @DeleteMapping()
    @ApiOperation("删除菜品")
    @CacheEvict(value = {"dishCategoryCache","setmealDishCache"}, allEntries = true)
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id查询菜品:{}", categoryId);
        List<Dish> dishList = dishService.getByCategoryId(categoryId);
        return Result.success(dishList);
    }

    /**
     * 更新菜品状态
     * @param status 状态
     * @param id 菜品id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("更新菜品状态")
    @CacheEvict(value = {"dishCategoryCache","setmealDishCache"}, allEntries = true)
    public Result updateStatus(@PathVariable Integer status, @RequestBody Long id) {
        log.info("更新菜品状态:{}", status);
        Dish dish = dishMapper.selectById(id);
        dish.setStatus(status);
        dishMapper.update(dish);
        return Result.success();
    }


}
