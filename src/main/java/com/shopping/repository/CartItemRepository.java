package com.shopping.repository;

import com.shopping.entity.CartItem;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByUserAndProduct(User user, Product product);
    List<CartItem> findAllByIdIn(List<Long> cartItemIds);
}
