package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.validation.errors.InputValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class MotorcycleTest {
    @Test
    void createMotorcycleOk() {
        String name = "Shorter than 255 characters";
        int engineSize = 500;
        var motorcycleInput = new MotorcycleInput(Brand.BMW, name, engineSize);
        var motorcycleMono = Motorcycle.of(motorcycleInput);

        StepVerifier
            .create(motorcycleMono)
            .expectNextMatches(m -> m.name().equals(name) && m.brand().equals(Brand.BMW) && m.engineSize().equals(engineSize))
            .expectComplete()
            .verify();
    }

    @Test
    void nameTooLong() {
        final String name = IntStream
            .range(0, 257)
            .mapToObj(i -> "a")
            .collect(Collectors.joining());
        var motorcycleInput = new MotorcycleInput(Brand.BMW, name, 500);
        var motorcycleMono = Motorcycle.of(motorcycleInput);

        StepVerifier
            .create(motorcycleMono)
            .expectErrorMatches(t -> t instanceof InputValidationException && t.getMessage().equals("Field name, constraint: length must be between 1 and 255, actual value: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."))
            .verify();
    }

    @Test
    void brandNull() {
        var motorcycleInput = new MotorcycleInput(null, "something", 500);
        var motorcycleMono = Motorcycle.of(motorcycleInput);

        StepVerifier
            .create(motorcycleMono)
            .expectErrorMatches(t -> t instanceof InputValidationException && t.getMessage().equals("Field brand, constraint: must not be null, actual value: null."))
            .verify();
    }

    @Test
    void everythingWrong() {
        var motorcycleInput = new MotorcycleInput(null, "", -1);
        var motorcycleMono = Motorcycle.of(motorcycleInput);

        StepVerifier
            .create(motorcycleMono)
            .expectErrorMatches(t -> t instanceof InputValidationException
                && t.getMessage().contains("Field name, constraint: length must be between 1 and 255, actual value: ")
                && t.getMessage().contains("Field name, constraint: must not be empty, actual value: ")
                && t.getMessage().contains("Field brand, constraint: must not be null, actual value: null")
                && t.getMessage().contains("Field engineSize, constraint: must be greater than or equal to 1, actual value: -1")
            )
            .verify();
    }
}