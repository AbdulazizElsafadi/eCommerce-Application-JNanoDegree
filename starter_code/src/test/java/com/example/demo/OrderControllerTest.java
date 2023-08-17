package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Transactional
@SpringBootTest(classes = SareetaApplication.class)
public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(new ArrayList<>());
        cart.setTotal(new BigDecimal(1));

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
//        when(UserOrder.createFromCart(user.getCart())).thenReturn(userOrder);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals( 1, response.getBody().getUser().getId());

    }

    @Test
    public void submitWithoutUser() {
        ResponseEntity<UserOrder> response = orderController.submit("any");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(new ArrayList<>());
        cart.setTotal(new BigDecimal(1));

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        userOrder.setItems(new ArrayList<>());
        userOrder.setTotal(new BigDecimal(1));

        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Long.valueOf(1), response.getBody().get(0).getId());
    }

    @Test
    public void getOrdersForUserWithoutUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("any");
        assertEquals(404, response.getStatusCodeValue());
    }
}
