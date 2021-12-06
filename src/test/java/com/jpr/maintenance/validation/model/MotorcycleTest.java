package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.model.Brand;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotorcycleTest {
    @Test
    void createMotorcycleOk() {
        final String name = "Shorter than 255 characters";
        var motorcycleInput = new MotorcycleInput(Brand.BMW, name, 500);
        var motorcycleEither = Motorcycle.of(motorcycleInput);

        assertTrue(motorcycleEither.isRight());
        var motorcycle = motorcycleEither.get();
        assertEquals(name, motorcycle.name());
        assertEquals(Brand.BMW, motorcycle.brand());
        assertEquals(500, motorcycle.engineSize());
    }

    @Test
    void nameTooLong() {
        final String name = IntStream
            .range(0, 257)
            .mapToObj(i -> "a")
            .collect(Collectors.joining());
        var motorcycleInput = new MotorcycleInput(Brand.BMW, name, 500);
        var motorcycleEither = Motorcycle.of(motorcycleInput);

        assertTrue(motorcycleEither.isLeft());
        assertEquals(INVALID_FIELD, motorcycleEither.getLeft().getErrorType());
        assertEquals("Invalid field: name, constraint: length must be between 1 and 255, invalid value: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", motorcycleEither.getLeft().getPath().get(0).toString());
    }

    @Test
    void brandNull() {
        var motorcycleInput = new MotorcycleInput(null, "something", 500);
        var motorcycleEither = Motorcycle.of(motorcycleInput);

        assertTrue(motorcycleEither.isLeft());
        assertEquals(INVALID_FIELD, motorcycleEither.getLeft().getErrorType());
        assertEquals("Invalid field: brand, constraint: must not be null, invalid value: null", motorcycleEither.getLeft().getPath().get(0).toString());
    }

    @Test
    void everythingWrong() {
        var motorcycleInput = new MotorcycleInput(null, "", -1);
        var motorcycleEither = Motorcycle.of(motorcycleInput);

        assertTrue(motorcycleEither.isLeft());
        assertEquals(INVALID_FIELD, motorcycleEither.getLeft().getErrorType());
        assertTrue(motorcycleEither.getLeft().getPath().contains("Invalid field: name, constraint: length must be between 1 and 255, invalid value: "));
        assertTrue(motorcycleEither.getLeft().getPath().contains("Invalid field: brand, constraint: must not be null, invalid value: null"));
        assertTrue(motorcycleEither.getLeft().getPath().contains("Invalid field: engineSize, constraint: must be greater than or equal to 1, invalid value: -1"));
        assertTrue(motorcycleEither.getLeft().getPath().contains("Invalid field: name, constraint: must not be empty, invalid value: "));
    }
}