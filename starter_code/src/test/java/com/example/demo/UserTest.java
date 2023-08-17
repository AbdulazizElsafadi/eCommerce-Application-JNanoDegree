package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

;

@Transactional
@SpringBootTest(classes = SareetaApplication.class)
public class UserTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUser() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = createUserRequest("test", "testPassword", "testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void createUserWithoutUsername() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("confirmPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserWithPasswordLessThan7() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("username");
        createUserRequest.setPassword("dd");
        createUserRequest.setConfirmPassword("dd");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findByID() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("1234567");
        user.setId(1);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        assertNotNull(user);

        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", response.getBody().getUsername());

    }

    @Test
    public void findByUsername() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("1234567");
        user.setId(1);

        when(userRepository.findByUsername("test")).thenReturn(user);

        assertNotNull(user);

        ResponseEntity<User> response = userController.findByUserName("test");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", response.getBody().getUsername());

    }

    private static CreateUserRequest createUserRequest(String username, String password, String confirmPassword) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(confirmPassword);
        return createUserRequest;
    }


}
