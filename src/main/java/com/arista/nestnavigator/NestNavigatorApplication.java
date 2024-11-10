package com.arista.nestnavigator;

//import com.arista.nestnavigator.amenity.entity.Amenity;
import com.arista.nestnavigator.amenity.service.AmenityService;
import com.arista.nestnavigator.property.entity.LandForSale;
import com.arista.nestnavigator.property.entity.Property;
import com.arista.nestnavigator.property.service.PropertyService;
import com.arista.nestnavigator.property.util.*;
import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.authorization.service.AuthenticationService;
import com.arista.nestnavigator.user.service.UserService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication

public class NestNavigatorApplication implements CommandLineRunner {
    private AuthenticationService authenticationService;
    private UserService userService;
    private PropertyService propertyService;
    private AmenityService amenityService;

    @Autowired
    public NestNavigatorApplication(@Lazy AuthenticationService authenticationService
            ,UserService userService,
                                    PropertyService propertyService,
                                    AmenityService amenityService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.propertyService = propertyService;
        this.amenityService = amenityService;
    }


    @Configuration
    public static class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .info(new Info()
                            .title("NestNavigator API")
                            .version("1.0")
                            .description("This is the API documentation for the NestNavigator application"));
        }
    }
    public static void main(String[] args){
        SpringApplication.run(NestNavigatorApplication.class, args);
    }
    @Override
    public void run(String ...args){
        // Create three users
        User user1 = new User("Siddu", "Tammireddy", "abc@gmail.com", "9876543210", "sidda_sai", "password");
        authenticationService.register(user1);
        user1 = userService.getUserByUsername("sidda_sai");

        User user2 = new User("John", "Doe", "john.doe@example.com", "1234567890", "john_doe", "password123");
        authenticationService.register(user2);
        user2 = userService.getUserByUsername("john_doe");

        User user3 = new User("Jane", "Smith", "jane.smith@example.com", "0987654321", "jane_smith", "securePass");
        authenticationService.register(user3);
        user3 = userService.getUserByUsername("jane_smith");
        amenityService.addAllAmenitiesToDatabase();
        // Create Property instances
        // Complete Property Object
        Property completeProperty = new Property();
        completeProperty.setTitle("Beautiful Family Home");
        completeProperty.setType(PropertyType.RESIDENTIAL.name());
        completeProperty.setPropertyCategory("Residential");
        completeProperty.setFacing(Directions.NORTH.name());
        completeProperty.setPropertyListingFor(PropertyListingType.SALE.name());
        completeProperty.setProjectName("Sunshine Villas");
//        completeProperty.setSubProperty(new SubProperty());
        completeProperty.setFurnitureStatus(Furniture.FULLY_FURNISHED.name());
        completeProperty.setFurnitureStatusDescription("Fully furnished with modern furniture");
        completeProperty.setDescription("A beautiful family home located in a serene environment.");
        completeProperty.setSuper_builtup_area(2500.0);
        completeProperty.setCarpet_area(2300.0);
        completeProperty.setPrice(500000.0);
        completeProperty.setAdvance(50000.0);
        completeProperty.setIsNegotiable(true);
//        completeProperty.setOwner(user1);
        completeProperty.setStatus(PropertyStatus.AVAILABLE.name());
//        completeProperty.setIsFeatured(true);
        completeProperty.setListedDate(LocalDateTime.now());
        completeProperty.setUpdatedDate(LocalDateTime.now());
        completeProperty.setExpiryDate(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        completeProperty.setListedby(ListedBy.OWNER.name());
        completeProperty.setContact("123-456-7890");
        completeProperty.setState("Andhra Pradesh");
        completeProperty.setCountry("India");
        completeProperty.setRevenueDivision("Srikakulam");
        completeProperty.setMandal("Mandal Example");
        completeProperty.setVillage("Village Example");
        completeProperty.setZip("123456");
        completeProperty.setLongitude("83.2185");
        completeProperty.setLatitude("17.6868");

        // Property Object missing some mandatory fields
//        Property incompleteProperty = new Property();
//        incompleteProperty.setTitle("Cozy Apartment");
//        incompleteProperty.setType(PropertyType.RESIDENTIAL);
//        incompleteProperty.setPropertyListingFor(PropertyListingType.RENT);
        // Missing category, facing direction, and other fields
//        incompleteProperty.setDescription("A cozy apartment suitable for small families.");
//        incompleteProperty.setSuper_builtup_area(1200.0);
//        incompleteProperty.setPrice(1500.0);
//        incompleteProperty.setOwner(new User());
//        incompleteProperty.setContact("098-765-4321");
//        incompleteProperty.setState("Andhra Pradesh");
//        incompleteProperty.setCountry("India");
//        incompleteProperty.setRevenueDivision("Srikakulam");
//        incompleteProperty.setMandal("Mandal Example");
//        incompleteProperty.setVillage("Village Example");
//        incompleteProperty.setZip("654321");

        // Property Object with minimal valid data
        Property minimalProperty = new Property();
        minimalProperty.setTitle("Studio Apartment");
        minimalProperty.setType(PropertyType.COMMERCIAL.name());
        minimalProperty.setPropertyCategory("Residential");
        minimalProperty.setFacing(Directions.EAST.name());
        minimalProperty.setPropertyListingFor(PropertyListingType.RENT.name());
        minimalProperty.setFurnitureStatus(Furniture.UNFURNISHED.name());
        minimalProperty.setDescription("A compact studio apartment ideal for bachelors.");
        minimalProperty.setSuper_builtup_area(600.0);
        minimalProperty.setPrice(800.0);
        minimalProperty.setIsNegotiable(false);
//        minimalProperty.setOwner(user2);
        minimalProperty.setStatus(PropertyStatus.AVAILABLE.name());
        minimalProperty.setListedby(ListedBy.DEALER.name());
        minimalProperty.setContact("456-789-0123");
        minimalProperty.setState("Andhra Pradesh");
        minimalProperty.setCountry("India");
        minimalProperty.setRevenueDivision("Srikakulam");
        minimalProperty.setMandal("Mandal Example");
        minimalProperty.setVillage("Village Example");
        minimalProperty.setZip("789012");
        propertyService.saveProperty(completeProperty,user1.getId());
        propertyService.saveProperty(minimalProperty,user2.getId());
        // Example usage of created objects
//        System.out.println(property1.toString());
//        System.out.println(property2.toString());
//        System.out.println(property3.toString());
    }

}
