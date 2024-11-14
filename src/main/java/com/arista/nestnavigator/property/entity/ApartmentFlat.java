package com.arista.nestnavigator.property.entity;

import com.arista.nestnavigator.property.util.PropertyType;
import jakarta.persistence.*;

@Entity
public class ApartmentFlat extends SubProperty{
    private static final PropertyType DEFAULT_PROPERTY_TYPE = PropertyType.RESIDENTIAL;
//    public ApartmentFlat() {
//        super();
//       this.setType(DEFAULT_PROPERTY_TYPE);
//    }
    @ManyToOne
    @JoinColumn(name = "subpropertyId")
    private SubProperty subpropertyId;
    @Column
    private String apartmentOrSocietyName;
    private String houseNo;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer balconies;
}
