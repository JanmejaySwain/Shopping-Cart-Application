package com.shopping.payload;

import lombok.Data;

import java.util.List;
@Data
public class CheckoutRequest {
    private Long userId;
    private List<Long> cartItemIds;

}
