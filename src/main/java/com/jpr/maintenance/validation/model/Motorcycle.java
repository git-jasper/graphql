package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.validation.ValidationService;
import reactor.core.publisher.Mono;

public record Motorcycle(Brand brand, String name, Integer engineSize) {
    public static Mono<Motorcycle> of(MotorcycleInput input) {
        return ValidationService.validate(input)
            .map(m ->
                new Motorcycle(
                    m.brand(),
                    m.name(),
                    m.engineSize()
                )
            );
    }
}
