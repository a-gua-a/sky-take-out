package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
     /**
      * 查看购物车
      */
     List<ShoppingCart> list();

     /**
      * 添加购物车
      */
     void add(ShoppingCartDTO shoppingCartDTO);

     /**
      * 清空购物车
      */
     void clean();

     /**
      * 删除购物车
      * @param shoppingCartDTO
      */
     void sub(ShoppingCartDTO shoppingCartDTO);
}
