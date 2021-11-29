package com.jpr.maintenance.validation.model.taskdetails;

import com.jpr.maintenance.graphql.model.TaskDetailsInput;
import graphql.GraphQLError;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskDetailsTest {

    @Test
    void createDetailsOk() {
        final String description = "HundredCharacterString    HundredCharacterString    HundredCharacterString    HundredCharacterString";
        TaskDetailsInput taskDetailsInput = new TaskDetailsInput(description, 5000, 48);
        Either<GraphQLError, TaskDetails> taskDetailsEither = TaskDetails.of(taskDetailsInput);

        assertTrue(taskDetailsEither.isRight());
        TaskDetails taskDetails = taskDetailsEither.get();
        assertEquals(description, taskDetails.getDescription());
        assertEquals(5000, taskDetails.getInterval_km());
        assertEquals(48, taskDetails.getInterval_months());
    }

    @Test
    void descriptionInvalidCharacter() {
        TaskDetailsInput taskDetailsInput = new TaskDetailsInput("some \n description", 5000, 48);
        Either<GraphQLError, TaskDetails> taskDetailsEither = TaskDetails.of(taskDetailsInput);

        assertTrue(taskDetailsEither.isLeft());
        assertEquals(INVALID_FIELD, taskDetailsEither.getLeft().getErrorType());
    }

    @Test
    void descriptionEmpty() {
        TaskDetailsInput taskDetailsInput = new TaskDetailsInput("", 5000, 48);
        Either<GraphQLError, TaskDetails> taskDetailsEither = TaskDetails.of(taskDetailsInput);

        assertTrue(taskDetailsEither.isLeft());
        assertEquals(INVALID_FIELD, taskDetailsEither.getLeft().getErrorType());
    }

    @Test
    void descriptionTooLong() {
        String tooLongString = "HundredAndOneCharacterString         HundredAndOneCharacterString        HundredAndOneCharacterString";
        TaskDetailsInput taskDetailsInput = new TaskDetailsInput(tooLongString, 5000, 48);
        Either<GraphQLError, TaskDetails> taskDetailsEither = TaskDetails.of(taskDetailsInput);

        assertTrue(taskDetailsEither.isLeft());
        assertEquals(INVALID_FIELD, taskDetailsEither.getLeft().getErrorType());
    }
}