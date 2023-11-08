package com.shopping.payload;

import lombok.Data;

@Data
public class CartItemUpdateRequest {
    private Long cartItemId;
    private int newQuantity;

}
