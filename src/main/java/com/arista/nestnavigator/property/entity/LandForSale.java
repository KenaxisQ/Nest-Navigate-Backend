package com.arista.nestnavigator.property.entity;

import com.arista.nestnavigator.property.util.Amenity;
import com.arista.nestnavigator.property.util.Directions;
import com.arista.nestnavigator.property.util.TaxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//@Entity
//@DiscriminatorValue("LAND_FOR_SALE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LandForSale{

@Enumerated(EnumType.STRING)
@Column(nullable = true)
private TaxStatus propertyTax;

@Column(nullable = true)
private String encumbrances;

@Column(nullable = false)
private double plotArea;

@Column(nullable = false)
private double length;

@Column(nullable = false)
private double width;

@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Directions facing;

@Column(nullable = true)
private boolean deposit;

@Column(nullable = true)
private String leaseTerms;

@Column(nullable = true)
private String renewalTerms;

@Column(nullable = true)
private boolean sublettingPolicy;

@Column(nullable = true)
private boolean mortgageTerms;

@Column(nullable = true)
private boolean financingOptionAvailable;

@Column(nullable = true)
private boolean mortgageTermsAvailable;

@Column(nullable = true)
private boolean sublettingPolicyAvailable;

@Column(nullable = true)
private boolean legalDocumentsAvailable;

@Column(nullable = true)
private String accessibility;

@ElementCollection(targetClass = Amenity.class)
@CollectionTable(name = "property_nearby_amenities", joinColumns = @JoinColumn(name = "property_id"))
@Enumerated(EnumType.STRING)
@Column(nullable = true)
private List<Amenity> nearbyAmenities;

@Column(nullable = true)
private String futureDevelopmentPlans;

}
