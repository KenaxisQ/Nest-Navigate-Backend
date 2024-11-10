package com.arista.nestnavigator.amenity.service;

import com.arista.nestnavigator.amenity.entity.Amenity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AmenityService {
    public List<Amenity> getAllAmenities();
    public Amenity createAmenity(Amenity amenity);
    public Amenity updateAmenity(Amenity amenity);
    public Amenity getAmenityFromName(String amenityName);
    public List<Amenity> getAmenityFromCategory(String category);
    public String deleteAmenityFromId(String amenityId);
    public String deleteAmenityFromName(String amenityName);
    public Amenity getAmenityFromId(String amenityId);
    public String addAllAmenitiesToDatabase();

}
