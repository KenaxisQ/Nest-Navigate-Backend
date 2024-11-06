package com.arista.nestnavigator.user.service;

import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.user.utils.ApiException;
import com.arista.nestnavigator.user.utils.ErrorCodes;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user!=null)
        return user;
        else throw new ApiException(ErrorCodes.USER_NOT_FOUND);
    }
}