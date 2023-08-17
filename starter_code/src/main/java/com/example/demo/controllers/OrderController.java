package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	Logger logger = LogManager.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		logger.info("OrderController: submit execution started...");
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("Exception: user doesn't exist: {}!", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		logger.info("submit: order saved {}!", username);
		logger.info("UserController: createUser execution ended...");
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		logger.info("OrderController: getOrdersForUser execution started...");
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("Exception: user doesn't exist: {}!", username);
			return ResponseEntity.notFound().build();
		}
		logger.info("getOrdersForUser: order is found {}!", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
