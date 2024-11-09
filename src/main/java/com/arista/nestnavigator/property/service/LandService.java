package com.arista.nestnavigator.property.service;

import com.arista.nestnavigator.property.entity.Property;
import com.arista.nestnavigator.property.repository.PropertyRepository;
import com.arista.nestnavigator.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LandService {

    PropertyRepository propertyRepository;
    UserService userService;
    public LandService(PropertyRepository propertyRepository, UserService userService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }
    public List<Object> getLandDetails(){
        return Collections.singletonList(propertyRepository.findAll());
    }
}
