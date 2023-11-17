package com.shopping.service.Impl;

import com.shopping.entity.CartItem;
import com.shopping.entity.Order;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.exception.CartItemNotFoundException;
import com.shopping.exception.InsufficientStockException;
import com.shopping.exception.QuantityLimitExceededException;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.payload.CartRequest;
import com.shopping.repository.CartItemRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.repository.UserRepository;
import com.shopping.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;
    private OrderRepository orderRepository;
    public CartServiceImpl(UserRepository userRepository ,ProductRepository productRepository,CartItemRepository cartItemRepository,OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository=productRepository;
        this.cartItemRepository=cartItemRepository;
        this.orderRepository=orderRepository;
    }

    @Override
    public CartItem addItemTocart(CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", cartRequest.getUserId()));
        System.out.println(user);
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product", "Product", cartRequest.getProductId()));
        System.out.println(product);
        if (cartRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (cartRequest.getQuantity() > product.getQuantityAvailable()) {
            throw new QuantityLimitExceededException(HttpStatus.BAD_REQUEST,"Quantity exceeds available stock");
        }
        CartItem existingCartItem=cartItemRepository.findByUserAndProduct(user, product);
        System.out.println(existingCartItem);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartRequest.getQuantity());
            CartItem savedCartItem = cartItemRepository.save(existingCartItem);
            System.out.println(savedCartItem);
            return savedCartItem;
        } else {
            CartItem cartItem = new CartItem(user, product,cartRequest.getQuantity());
            return cartItemRepository.save(cartItem);
        }
    }

    @Override
    public String updateCartItemQuantity(Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("cartItem", "cartItemId", cartItemId));
        if(newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        Product product = cartItem.getProduct();
        if (newQuantity > product.getQuantityAvailable()) {
            throw new QuantityLimitExceededException(HttpStatus.BAD_REQUEST,"Quantity exceeds available stock");
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return  "Cart Updated Successfully";
    }

    @Override
    public Order checkout(Long userId, List<Long> cartItemIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        System.out.println(user);
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);
        System.out.println(cartItems);
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException(HttpStatus.NOT_FOUND,"No cart items found for checkout");
        }
        double totalAmount = 0.0;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (cartItem.getQuantity() > product.getQuantityAvailable()) {
                throw new InsufficientStockException(HttpStatus.BAD_REQUEST,"Insufficient stock for checkout");
            }
            totalAmount += product.getPrice().doubleValue() * cartItem.getQuantity();
            product.setQuantityAvailable(product.getQuantityAvailable() - cartItem.getQuantity());
            Product savedProduct = productRepository.save(product);
            System.out.println(savedProduct);
            cartItemRepository.delete(cartItem);

        }
        Order order = new Order(user, totalAmount);
        order.setCartItems(cartItems);
        orderRepository.save(order);
        System.out.println("Order :"+order);
        return order;
    }

}
