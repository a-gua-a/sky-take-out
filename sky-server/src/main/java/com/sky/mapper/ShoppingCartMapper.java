package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
      * 查看购物车
     * @param shoppingCart
      */
     List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

     /**
      * 更新购物车
      * @param cart
      */
    void update(ShoppingCart cart);

     /**
      * 删除购物车
      * @param shoppingCart
      */
    void delete(ShoppingCart shoppingCart);
}
