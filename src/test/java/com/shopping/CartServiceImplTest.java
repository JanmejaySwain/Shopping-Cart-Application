package com.shopping;

import com.shopping.entity.CartItem;
import com.shopping.entity.Order;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.payload.CartRequest;
import com.shopping.repository.CartItemRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.repository.UserRepository;
import com.shopping.service.Impl.CartServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.Invocation;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CartServiceImpl cartService;
    private CartRequest cartRequest;
    private User user;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    public void init() {
        cartRequest = CartRequest.builder().userId(1L).quantity(2).productId(1L).build();
        user = User.builder().id(1L).username("Janmejay").email("janmejaswain@2001@gmail.com").build();
        product = Product.builder().name("jeans").price(BigDecimal.valueOf(100)).description("pant").quantityAvailable(200).build();
        cartItem = CartItem.builder().id(1L).user(user).order(null).total(BigDecimal.valueOf(500)).product(product).quantity(5).build();
    }

    @Test
    @DisplayName("Add to Cart Test")
    public void addItemToCartTest() {
        when(userRepository.findById(cartRequest.getUserId())).thenReturn(Optional.ofNullable(user));
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
//        when(cartItemRepository.findByUserAndProduct())
//        given(cartItemRepository.findByUserAndProduct(ArgumentMatchers.any(), ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));
//        given(cartItemRepository.save(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));
        when(cartItemRepository.findByUserAndProduct(user,product)).thenReturn(cartItem);
        when(cartItemRepository.save(ArgumentMatchers.any())).thenReturn(cartItem);
        CartItem addedItem = cartService.addItemTocart(cartRequest);
        Assertions.assertThat(addedItem).isNotNull();
    }
    @Test
    @DisplayName("update cart test")
    public  void updateCartItemQuantityTest(){
        long cartItemId=1;
        int newQuantity=4;
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.ofNullable(cartItem));
        when(cartItemRepository.save(ArgumentMatchers.any())).thenReturn(cartItem);
        String s = cartService.updateCartItemQuantity(cartItemId, newQuantity);
        assertEquals(s,"Cart Updated Successfully",s);
    }
    @Test
    @DisplayName("check out test")
    public void checkoutTest(){
        long userId=1;
        List<Long> cartItemIds= Arrays.asList(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(cartItemRepository.findAllById(cartItemIds)).thenReturn(Stream.of(new CartItem(user,product,2)).collect(Collectors.toList()));
    when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
//     when(cartItemRepository.delete(cartItem)).thenReturn(null);
//        doAnswer(invocationOnMock -> {
//            return null;
//        }).when(cartItemRepository.delete(any()));
        doNothing().when(cartItemRepository).delete(any(CartItem.class));
        when(orderRepository.save(any(Order.class))).thenReturn(any(Order.class));
        Order order = cartService.checkout(userId, cartItemIds);
        Assertions.assertThat(order).isNotNull();
    }

}
