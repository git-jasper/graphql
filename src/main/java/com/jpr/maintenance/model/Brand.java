package com.jpr.maintenance.model;

import lombok.Getter;

@Getter
public enum Brand {
    APRILLA("Aprilla"),
    BMW("BMW"),
    DUCATI("Ducati"),
    HARLEY_DAVIDSON("Harley Davidson"),
    HONDA("Honda"),
    INDIAN("Indian Motorcycle"),
    KAWASAKI("Kawasaki"),
    KTM("KTM"),
    ROYAL_ENFIELD("Royal Enfield"),
    SUZUKI("Suzuki"),
    TRIUMPH("Triumph"),
    YAMAHA("Yamaha");

    private final String brandName;

    Brand(String brandName) {
        this.brandName = brandName;
    }
}
