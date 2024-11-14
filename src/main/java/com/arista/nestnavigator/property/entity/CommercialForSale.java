package com.arista.nestnavigator.property.entity;

import com.arista.nestnavigator.property.util.Furniture;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

//@Entity
//@DiscriminatorValue("COMMERCIAL_FOR_SALE")
public class CommercialForSale {

//    @Column(nullable = false)
//    private Furniture furniturestatus;

    @Column(nullable = false)
    private Double super_Builtup_area_sqft;

    @Column(nullable = false)
    private Double carpet_area_sqft;

    @Column(nullable = true)
    private Double monthly_maintainance;

    @Column(nullable = false)
    private int Carparking;

    @Column(nullable = false)
    private int Washrooms;
}
