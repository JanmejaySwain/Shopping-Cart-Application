package com.shopping.controller;

import com.shopping.entity.CartItem;
import com.shopping.entity.Order;
import com.shopping.payload.CartItemUpdateRequest;
import com.shopping.payload.CartRequest;
import com.shopping.payload.CheckoutRequest;
import com.shopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody CartRequest cartRequest) {
        return new ResponseEntity<>(cartService.addItemTocart(cartRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestBody CartItemUpdateRequest request) {

        return new ResponseEntity<>(cartService.updateCartItemQuantity(request.getCartItemId(), request.getNewQuantity()),HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody CheckoutRequest request) {
        return  new ResponseEntity<>(cartService.checkout(request.getUserId(), request.getCartItemIds()),HttpStatus.OK);
    }
}
