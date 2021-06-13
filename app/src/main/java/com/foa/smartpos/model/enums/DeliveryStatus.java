package com.foa.smartpos.model.enums;

public enum DeliveryStatus {
    DRAFT,
    ASSIGNING_DRIVER,
    ON_GOING,
    PICKED_UP ,
    COMPLETED ,
    CANCELLED ,
    EXPIRED
    //ON_GOING OR PICKED_UP = ACTIVE
}
