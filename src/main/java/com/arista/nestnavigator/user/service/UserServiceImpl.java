package com.arista.nestnavigator.user.service;

import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.custom_exceptions.ApiException;
import com.arista.nestnavigator.custom_exceptions.ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
    this.userRepository =userRepository;
    }

    @Override
    public String createUser(User user) {
        userRepository.save(user);
        return "User Creation Successful";
    }

    @Override
    public String updateUser(User user) {
        userRepository.save(user);
        return "User updated Successfully" ;
    }

    @Override
    public String deleteUser(String id) {
       userRepository.deleteById(id);
        return "User Deleted Successfully" ;
    }

    @Override
    public User getUser(String id) {
        if(userRepository.findById(id).isEmpty())
            throw new ApiException(ErrorCodes.USER_NOT_FOUND);
        else return userRepository.findById(id).get();
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email){
            return userRepository.getUserByEmail(email);
    }
    @Override
    public User getUserByPhone(String email){
        return userRepository.getUserByPhone(email);
    }

    @Override
    public User getUserByUsername(String username){return userRepository.getUserByUsername(username);}

}

