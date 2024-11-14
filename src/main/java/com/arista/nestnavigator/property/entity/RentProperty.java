package com.arista.nestnavigator.property.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Entity
//@DiscriminatorValue("RENT")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class RentProperty {

    @Column(nullable = false)
    private double monthlyRentPrice;
    @Column(nullable = true)
    private double depositRequirements;
    @Column(nullable = false)
    private String leaseDuration;
    @Column(nullable = false)
    private LocalDateTime moveInDate;
    @Column(nullable = true)
    private boolean SublettingPolicy;
    @Column(nullable = true)
    private String LeaseTerm;
    @Column(nullable = true)
    private String RenewalTerms;
    @Column(nullable = false)
    private Double MonthlyMaintainance;


}
