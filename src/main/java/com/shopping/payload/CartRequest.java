package com.shopping.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartRequest {
    private Long userId;
    private Long productId;
    private int quantity;

}
