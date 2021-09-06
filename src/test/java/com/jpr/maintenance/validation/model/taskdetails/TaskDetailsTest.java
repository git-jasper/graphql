package com.jpr.maintenance.validation.model.taskdetails;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.schema.DataFetchingEnvironmentImpl;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskDetailsTest {

    @Test
    void createDetailsOk() {
        final String description = "HundredCharacterString    HundredCharacterString    HundredCharacterString    HundredCharacterString";
        Map<String, Object> arguments = Map.of(
            "description", description,
            "interval_km", "5000",
            "interval_months", "48"
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        Either<InputValidationError, TaskDetails> taskDetailsEither = TaskDetails.of(environment);

        assertTrue(taskDetailsEither.isRight());
        TaskDetails taskDetails = taskDetailsEither.get();
        assertEquals(description, taskDetails.getDescription());
        assertEquals(5000, taskDetails.getInterval_km());
        assertEquals(48, taskDetails.getInterval_months());
    }

    @Test
    void descriptionInvalidCharacter() {
        Map<String, Object> arguments = Map.of(
            "description", "some \n description",
            "interval_km", 5000,
            "interval_months", 48
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        Either<InputValidationError, TaskDetails> taskDetailsEither = TaskDetails.of(environment);

        assertTrue(taskDetailsEither.isLeft());
        assertEquals(INVALID_DESCRIPTION, taskDetailsEither.getLeft());
    }

    @Test
    void descriptionEmpty() {
        Map<String, Object> arguments = Map.of(
            "description", "",
            "interval_km", 5000,
            "interval_months", 48
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        Either<InputValidationError, TaskDetails> taskDetailsEither = TaskDetails.of(environment);

        assertTrue(taskDetailsEither.isLeft());
        assertEquals(INVALID_DESCRIPTION, taskDetailsEither.getLeft());
    }

    @Test
    void descriptionTooLong() {
        Map<String, Object> arguments = Map.of(
            "description", "HundredAndOneCharacterString         HundredAndOneCharacterString        HundredAndOneCharacterString",
            "interval_km", 5000,
            "interval_months", 48
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        Either<InputValidationError, TaskDetails> taskDetailsEither = TaskDetails.of(environment);

        assertTrue(taskDetailsEither.isLeft());
        assertEquals(INVALID_DESCRIPTION, taskDetailsEither.getLeft());
    }
}