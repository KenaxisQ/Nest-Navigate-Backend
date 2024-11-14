package com.arista.nestnavigator.property.service;

import com.arista.nestnavigator.property.entity.Property;

import java.util.List;

public interface PropertyService {
    Property getPropertyById(String id);
    Property saveProperty(Property property, String userId);
    List<Property> getAllProperties();
    String deleteProperty(String id);
    Property updateProperty(Property property);
}
