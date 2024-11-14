package com.arista.nestnavigator.property_amenities.service;

import com.arista.nestnavigator.amenity.entity.Amenity;
import com.arista.nestnavigator.property.entity.Property;
import com.arista.nestnavigator.property_amenities.entity.PropertyAmenities;

import java.util.List;

public interface PropertyAmenitiesService {
    List<Property> getPropertiesHavingAmenityId(String id);
    List<Property> getPropertiesHavingAmenityName(String amenityName);
    List<Amenity> getAmenitiesOfPropertyByPropertyId(String propertyId);
    PropertyAmenities createPropertyAmenities(PropertyAmenities propertyAmenities);
    PropertyAmenities updatePropertyAmenities(PropertyAmenities propertyAmenities);
    PropertyAmenities deletePropertyAmenities(PropertyAmenities propertyAmenities);
    PropertyAmenities getPropertyAmenitiesFromId(String propertyAmenitiesId);
    List<PropertyAmenities> getAllPropertyAmenities();
}
