package com.arista.nestnavigator.property.repository;

import com.arista.nestnavigator.property.entity.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, String> {
    Property findPropertyById(String id);
}
