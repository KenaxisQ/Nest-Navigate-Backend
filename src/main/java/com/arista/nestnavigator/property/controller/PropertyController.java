package com.arista.nestnavigator.property.controller;

import com.arista.nestnavigator.property.entity.Property;
import com.arista.nestnavigator.custom_exceptions.ApiException;
import com.arista.nestnavigator.property.service.PropertyService;
import com.arista.nestnavigator.user.utils.ApiResponse;
import com.arista.nestnavigator.user.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/api/property")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(@Lazy PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<Property>>> getLandDetails(){
        try{
            List<Property> propertyList = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            propertyList=propertyService.getAllProperties();
            long executionTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.ok(ResponseBuilder.success(propertyList,"Property Retrieved Successfully",executionTime));
        }
        catch (Exception ex){
            throw new ApiException("LAND_RETRIEVING","Error While Retrieving lands", HttpStatus.BAD_REQUEST);
        }
    }
        @PostMapping("/create")
        public ResponseEntity<ApiResponse<Property>> createProperty(@RequestBody Property property){
        propertyService.saveProperty(property);
        return ResponseEntity.ok(ResponseBuilder.success(property,"Property Created Successfully"));

        }
}
