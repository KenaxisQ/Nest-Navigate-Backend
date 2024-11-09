package com.arista.nestnavigator.property_amenities.controller;

import com.arista.nestnavigator.amenity.entity.Amenity;
import com.arista.nestnavigator.property.entity.Property;
import com.arista.nestnavigator.property_amenities.entity.PropertyAmenities;
import com.arista.nestnavigator.property_amenities.service.PropertyAmenitiesService;
import com.arista.nestnavigator.user.utils.ApiResponse;
import com.arista.nestnavigator.user.utils.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/propertyamenities")
public class PropertyAmenitiesController {
    PropertyAmenitiesService propertyAmenitiesService;
    public PropertyAmenitiesController(PropertyAmenitiesService propertyAmenitiesService){
        this.propertyAmenitiesService = propertyAmenitiesService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyAmenities>>> getAllPropertyAmenities(){
        return ResponseEntity.ok(ResponseBuilder.success(propertyAmenitiesService.getAllPropertyAmenities()));
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse<PropertyAmenities>>getPropertyAmenitiesFromId(@PathVariable String id){
        return ResponseEntity.ok(ResponseBuilder.success(propertyAmenitiesService.getPropertyAmenitiesFromId(id)));
    }
    @GetMapping("/amenityid/{id}")
    public ResponseEntity<ApiResponse<List<Property>>> getPropertiesHavingAmenityId(@PathVariable String id){
        return ResponseEntity.ok(ResponseBuilder.success(propertyAmenitiesService.getPropertiesHavingAmenityId(id)));
    }
    @GetMapping("/amenityname/{name}")
    public ResponseEntity<ApiResponse<List<Property>>> getPropertiesHavingAmenityName(@PathVariable String name){
        return ResponseEntity.ok(ResponseBuilder.success(propertyAmenitiesService.getPropertiesHavingAmenityName(name)));
    }
    @GetMapping("/propertyid/{propertyId}")
    public ResponseEntity<ApiResponse<List<Amenity>>> getAmenitiesOfPropertyByPropertyId(@PathVariable String propertyId){
        return ResponseEntity.ok(ResponseBuilder.success(propertyAmenitiesService.getAmenitiesOfPropertyByPropertyId(propertyId)));
    }
//    @GetMapping
}
