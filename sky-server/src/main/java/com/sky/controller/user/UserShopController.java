package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/shop")
@Api(tags = "用户端店铺接口")
public class UserShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺状态
     */
    @ApiOperation("获取店铺状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("shop_status");
        return Result.success(status);
    }
}
