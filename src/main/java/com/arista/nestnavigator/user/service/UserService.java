package com.arista.nestnavigator.user.service;

import com.arista.nestnavigator.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public String createUser(User user);
    public String updateUser(User user);
    public String deleteUser(String id);
    public User getUser(String id);
    public List<User> getUsers();
    public User getUserByEmail(String email);
    public User getUserByPhone(String phone);
    public User getUserByUsername(String username);
}