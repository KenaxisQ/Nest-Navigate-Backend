package com.arista.nestnavigator.user.controller;

import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.user.service.UserService;

import com.arista.nestnavigator.custom_exceptions.ApiException;
import com.arista.nestnavigator.user.utils.ApiResponse;
import com.arista.nestnavigator.custom_exceptions.ErrorCodes;
import com.arista.nestnavigator.user.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

import java.util.List;

@RestController
@RequestMapping("v1/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserDetailsById(@PathVariable @NotBlank String id) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info(String.format("User controller: getUserDetailsById() : Fetching User From DB for user id : %s", id));
            User user = userService.getUser(id);
            if (user == null) throw new ApiException(ErrorCodes.USER_NOT_FOUND);

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info(String.format("User Controller: getUserById() : Fetch Successful for UserID: %s", id));
            return ResponseEntity.ok(ResponseBuilder.success(
                    user,
                    "User Fetch Successful",
                    executionTime
            ));
        } catch (ApiException e) {
            logger.error("Error While Retrieving UserId : " + id);
            e.printStackTrace();
            throw e;  // Re-throwing ApiException to ensure correct error codes are propagated
        } catch (Exception e) {
            logger.error("Error While Retrieving UserId : " + id);
            e.printStackTrace();
            throw new ApiException("UNKNOWN_EXCEPTION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        long startTime = System.currentTimeMillis();
        logger.info("User Controller: getAllUsers() : Fetching all the Users from DB....");
        List<User> usersList = userService.getUsers();
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info(String.format("User Controller: getAllUsers() : Fetch Successful, Execution Time: %s", executionTime));
        if (!usersList.isEmpty())
            return ResponseEntity.ok(ResponseBuilder.success(usersList, "Users Fetch Successful", executionTime));
        else throw new ApiException(ErrorCodes.USER_EMPTY);
    }
    @PutMapping
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User user) {
        long startTime = System.currentTimeMillis();
        logger.info(String.format("User Controller: updateUser(): Updating User: %s %s", user.getLastname(), user.getFirstname()));

        try {
            User existingUser = userService.getUser(user.getId());
            if (existingUser == null) {
                throw new ApiException(ErrorCodes.USER_NOT_FOUND);
            }
            if (user.getEmail() != null) {
                User userByEmail = userService.getUserByEmail(user.getEmail());
                if (userByEmail != null && !userByEmail.getId().equals(user.getId())) {
                    throw new ApiException(ErrorCodes.EMAIL_ALREADY_EXISTS);
                }
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPhone() != null) {
                User userByPhone = userService.getUserByPhone(user.getPhone());
                if (userByPhone != null && !userByPhone.getId().equals(user.getId())) {
                    throw new ApiException(ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS);
                }
                existingUser.setPhone(user.getPhone());
            }
            if (user.getUsername() != null) {
                User userByUsername = userService.getUserByUsername(user.getUsername());
                if (userByUsername != null && !userByUsername.getId().equals(user.getId())) {
                    throw new ApiException(ErrorCodes.USER_ALREADY_EXISTS);
                }
                existingUser.setUsername(user.getUsername());
            }
            if (user.getFirstname().equals(existingUser.getFirstname()) &&
                    user.getLastname().equals(existingUser.getLastname()) &&
                    user.getEmail().equals(existingUser.getEmail()) &&
                    user.getPhone().equals(existingUser.getPhone()) &&
                    user.getUsername().equals(existingUser.getUsername()) &&
                    user.getPassword().equals(existingUser.getPassword()) &&
                    user.getRole().equals(existingUser.getRole()) &&
                    user.getProperties_listed() == existingUser.getProperties_listed() &&
                    user.getProperties_listing_limit() == existingUser.getProperties_listing_limit() &&
                    user.isActive() == existingUser.isActive()) {
                throw new ApiException("NO_CHANGES_DETECTED", "No changes detected. All fields are the same.", HttpStatus.BAD_REQUEST);
            }
            if (user.getFirstname() != null) existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null) existingUser.setLastname(user.getLastname());
            if (user.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole() != null) existingUser.setRole(user.getRole());
            if (user.getProperties_listed() > 0) existingUser.setProperties_listed(user.getProperties_listed());
            if (user.getProperties_listing_limit() > 0)
                existingUser.setProperties_listing_limit(user.getProperties_listing_limit());
            existingUser.setActive(user.isActive());
            userService.updateUser(existingUser);
            long executionTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.ok(ResponseBuilder.success(existingUser, "User updated successfully", executionTime));
        } catch (ApiException ex) {
            logger.error("API error while updating user: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while updating user: " + ex.getMessage());
            throw new ApiException("UNKNOWN_EXCEPTION", ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        long executionTime;
        try {
            long startTime = System.currentTimeMillis();
            if (userService.getUser(id) == null)
                throw new ApiException(ErrorCodes.USER_NOT_FOUND);
            else {
                userService.deleteUser(id);
                executionTime = System.currentTimeMillis() - startTime;
            }
        } catch (ApiException ex) {
            logger.error("API error while deleting user: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting user: " + ex.getMessage());
            throw new ApiException("UNKNOWN_EXCEPTION", ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ResponseBuilder.success(id, "User Deleted Successfully", executionTime));
    }
}