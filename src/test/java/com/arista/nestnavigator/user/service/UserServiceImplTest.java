package com.arista.nestnavigator.user.service;

import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.custom_exceptions.ApiException;
import com.arista.nestnavigator.custom_exceptions.ErrorCodes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    UserService userService;
    User existingUser;
    List<User> userList = new ArrayList<>();
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
        existingUser = new User("John", "Doe", "john.doe@example.com", "1234567890", "john_doe", "password123");
        userList.add(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getUserById_Success_Test() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        User user = userService.getUser(existingUser.getId());
        assertThat(user).isEqualTo(existingUser);
    }

    @Test
    void getUserById_Error_Test() {
        when(userRepository.findById("123")).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> userService.getUser("123"));
    }

    @Test
    void getUserByUsername_Success_Test() {
        when(userRepository.getUserByUsername(existingUser.getUsername())).thenReturn(existingUser);
        User user = userService.getUserByUsername(existingUser.getUsername());
        assertThat(user).isEqualTo(existingUser);
    }

    @Test
    void getUserByEmail_Success_Test() {
        when(userRepository.getUserByEmail(existingUser.getEmail())).thenReturn(existingUser);
        User user = userService.getUserByEmail(existingUser.getEmail());
        assertThat(user).isEqualTo(existingUser);
    }

    @Test
    void getUserByPhone_Success_Test() {
        when(userRepository.getUserByPhone(existingUser.getPhone())).thenReturn(existingUser);
        User user = userService.getUserByPhone(existingUser.getPhone());
        assertThat(user).isEqualTo(existingUser);
    }

    @Test
    void getUsers_Success_Test() {
        when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.getUsers();
        assertThat(users).isEqualTo(userList);
    }

    @Test
    void createUser_Success_Test() {
        User newUser = new User("New", "User", "new.user@example.com", "9876543210", "new_user", "newpassword123");
        when(userRepository.save(newUser)).thenReturn(newUser);
        String result = userService.createUser(newUser);
        assertThat(result).isEqualTo("User Creation Successful");
    }

    @Test
    void createUser_Error_Test() {
        User faultyUser = new User("", "", "", "", "", "");
        when(userRepository.save(any(User.class))).thenThrow(new ApiException(ErrorCodes.USER_EMPTY));
        assertThrows(ApiException.class, () -> userService.createUser(faultyUser));
    }

    @Test
    void updateUser_Success_Test() {
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        String result = userService.updateUser(existingUser);
        assertEquals("User updated Successfully", result);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void deleteUser_Success_Test() {
        doNothing().when(userRepository).deleteById("1");
        String result = userService.deleteUser("1");
        assertEquals("User Deleted Successfully", result);
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteUser_NotFound_Test() {
        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById("2");
        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class,
                () -> userService.deleteUser("2"));
        assertNotNull(exception);
    }

    @Test
    void getUsers_Empty_Test() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<User> users = userService.getUsers();
        assertTrue(users.isEmpty());
    }
}