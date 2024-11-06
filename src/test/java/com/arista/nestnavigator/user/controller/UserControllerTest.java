package com.arista.nestnavigator.user.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.user.service.UserService;
import com.arista.nestnavigator.user.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Logger logger;

    private User existingUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        existingUser = new User("1", "John", "Doe", "john.doe@example.com",
                "1234567890", "john_doe", "password123", UserRole.USER, 0, 5,
                true, LocalDateTime.now());
    }

    @Test
    public void testGetUserDetailsById_Success() {
        when(userService.getUser("1")).thenReturn(existingUser);

        ResponseEntity<ApiResponse<User>> response = userController.getUserDetailsById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<User> body = response.getBody();
        assertNotNull(body);
        assertEquals(existingUser, body.getData());
        assertEquals("User Fetch Successful", body.getMessage());
    }

    @Test
    public void testGetUserDetailsById_UserNotFound() {
        when(userService.getUser("1")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.getUserDetailsById("2"));

        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    public void testGetAllUsers_Success() {
        List<User> userList = Collections.singletonList(existingUser);
        when(userService.getUsers()).thenReturn(userList);

        ResponseEntity<ApiResponse<List<User>>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<List<User>> body = response.getBody();
        assertNotNull(body);
        assertEquals(userList, body.getData());
        assertEquals("Users Fetch Successful", body.getMessage());
    }

    @Test
    public void testGetAllUsers_EmptyList() {
        when(userService.getUsers()).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.getAllUsers());

        assertEquals(ErrorCodes.USER_EMPTY.getCode(), exception.getErrorCode());
    }

    @Test
    public void testCreateUser_Success() {
        User newUser = new User("USR_1", "Jane", "Doe", "jane.doe@example.com",
                "0987654321", "jane_doe", "password123", UserRole.USER, 0, 5,
                true, LocalDateTime.now());
        when(userService.getUserByUsername("jane_doe")).thenReturn(null);
        when(userService.getUserByEmail("jane.doe@example.com")).thenReturn(null);
        when(userService.getUserByPhone("0987654321")).thenReturn(null);
        when(userService.createUser(newUser)).thenReturn(String.valueOf(newUser));
        ResponseEntity<ApiResponse<User>> response = userController.createUser(newUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<User> body = response.getBody();
        assertNotNull(body);
        assertEquals(newUser, body.getData());
        assertEquals("User Created Successfully", body.getMessage());
    }

    @Test
    public void testCreateUser_MissingFields() {
        User newUser = new User();

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.createUser(newUser));

        assertEquals("USER_INFO_MISSING", exception.getErrorCode());
    }

    @Test
    public void testCreateUser_DuplicateProperties() {
        User newUser = new User("USR_1", "Jane", "Doe", "jane.doe@example.com",
                "0987654321", "jane_doe", "password123", UserRole.USER, 0, 5,
                true, LocalDateTime.now());

        when(userService.getUserByUsername("jane_doe")).thenReturn(existingUser);
        when(userService.getUserByEmail("jane.doe@example.com")).thenReturn(null);
        when(userService.getUserByPhone("0987654321")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.createUser(newUser));

        assertEquals("DUPLICATE_PROPERTIES", exception.getErrorCode());
    }

    @Test
    public void testUpdateUser_Success() {
        User updatedUser = new User("1", "John", "DoeUpdated", "john.doe@example.com",
                "1234567890", "john_doe", "password123Updated", UserRole.ADMIN, 1,
                10, false, LocalDateTime.now());

        when(userService.getUser("1")).thenReturn(existingUser);

        ResponseEntity<ApiResponse<User>> response = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<User> body = response.getBody();
        assertNotNull(body);
        assertEquals("User updated successfully", body.getMessage());
        assertEquals(updatedUser.getLastname(), body.getData().getLastname());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User updatedUser = new User();
        updatedUser.setId("2");

        when(userService.getUser("2")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.updateUser(updatedUser));

        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), exception.getErrorCode());
    }

    @Test
    public void testUpdateUser_EmailAlreadyExists() {
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setEmail("new.email@example.com");

        when(userService.getUser("1")).thenReturn(existingUser);
        when(userService.getUserByEmail("new.email@example.com")).thenReturn(new User("2", null, null, null, null, null, null, null, 0, 0, false, null));

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.updateUser(updatedUser));

        assertEquals(ErrorCodes.EMAIL_ALREADY_EXISTS.getCode(), exception.getErrorCode());
    }

    @Test
    public void testUpdateUser_PhoneAlreadyExists() {
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setPhone("0987654321");

        when(userService.getUser("1")).thenReturn(existingUser);
        when(userService.getUserByPhone("0987654321")).thenReturn(new User("2", null, null, null, null, null, null, null, 0, 0, false, null));

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.updateUser(updatedUser));

        assertEquals(ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS.getCode(), exception.getErrorCode());
    }

    @Test
    public void testUpdateUser_UsernameAlreadyExists() {
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setUsername("new_username");

        when(userService.getUser("1")).thenReturn(existingUser);
        when(userService.getUserByUsername("new_username")).thenReturn(new User("2", null, null, null, null, null, null, null, 0, 0, false, null));

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.updateUser(updatedUser));

        assertEquals(ErrorCodes.USER_ALREADY_EXISTS.getCode(), exception.getErrorCode());
    }

    @Test
    public void testUpdateUser_NoChangesDetected() {
        User updatedUser = new User("1", "John", "Doe", "john.doe@example.com",
                "1234567890", "john_doe", "password123", UserRole.USER, 0, 5,
                true, LocalDateTime.now());

        when(userService.getUser("1")).thenReturn(existingUser);

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.updateUser(updatedUser));

        assertEquals("No changes detected. All fields are the same.", exception.getMessage());
    }

    @Test
    public void testDeleteUser_Success() {
        when(userService.getUser("1")).thenReturn(existingUser);
        when(userService.deleteUser("1")).thenReturn(String.valueOf(true)); // Adjust return type as necessary

        ResponseEntity<ApiResponse<String>> response = userController.deleteUser("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertEquals("User Deleted Successfully", body.getMessage());
        assertEquals("1", body.getData());
    }
    @Test
    public void testDeleteUser_UserNotFound() {
        when(userService.getUser("1")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class,
                () -> userController.deleteUser("1"));

        assertEquals(ErrorCodes.USER_NOT_FOUND.getCode(), exception.getErrorCode());
    }
}