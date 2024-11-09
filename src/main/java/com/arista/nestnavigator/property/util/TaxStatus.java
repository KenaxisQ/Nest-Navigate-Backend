package com.arista.nestnavigator.property.util;

public enum TaxStatus {
    UP_TO_DATE("UP-TO-DATE"),
    PENDING("PENDING");

    private String status;
    TaxStatus(String status){
        this.status = status;
    }
}
