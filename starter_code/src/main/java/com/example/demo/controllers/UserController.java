package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        logger.info("UserController: findById execution started...");
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        logger.info("UserController: findByUserName execution started...");
        User user = userRepository.findByUsername(username);
        logger.info("UserController: user is {}", user);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        logger.info("UserController: createUser execution started...");
        if (createUserRequest.getUsername() == null || createUserRequest.getPassword() == null || createUserRequest.getConfirmPassword() == null) {
            logger.error("you didn't provider username or password or confirmPassword!");
            return ResponseEntity.badRequest().build();
        }

        if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            logger.error("password is either smaller than 7 digits or confirm password is not the same as the password!");
            return ResponseEntity.badRequest().build();
        }

        if (userRepository.findByUsername(createUserRequest.getUsername()) != null) {
            logger.error("username already exists {}", createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        logger.info("User created successfully with username: {}", user.getUsername());
        logger.info("UserController: createUser execution ended...");

        return ResponseEntity.ok(user);
    }

}
