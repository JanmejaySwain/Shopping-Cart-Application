package com.shopping.service;

import com.shopping.entity.CartItem;
import com.shopping.entity.Order;
import com.shopping.payload.CartRequest;

import java.util.List;

public interface CartService {
    CartItem addItemTocart(CartRequest cartRequest);

    String updateCartItemQuantity(Long cartItemId, int newQuantity);

    Order checkout(Long userId, List<Long> cartItemIds);
}
