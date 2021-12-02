package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.validation.ValidationService;
import graphql.GraphQLError;
import io.vavr.control.Either;

public record Motorcycle(Brand brand, String name, Integer engineSize) {
    public static Either<GraphQLError, Motorcycle> of(MotorcycleInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> Either.right(
                new Motorcycle(
                    input.brand(),
                    input.name(),
                    input.engineSize()
                )
            ));
    }
}
