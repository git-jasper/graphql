package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.UserInput;
import org.junit.jupiter.api.Test;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void createUserOk() {
        var userInput = new UserInput("myName", "myPassword");
        var userEither = User.of(userInput);

        assertTrue(userEither.isRight());
        var user = userEither.get();
        assertEquals("myName", user.username());
        assertEquals("myPassword", user.password());
    }

    @Test
    void invalidName() {
        var userInput = new UserInput("", "myPassword");
        var userEither = User.of(userInput);

        assertTrue(userEither.isLeft());
        assertEquals(INVALID_FIELD, userEither.getLeft().getErrorType());
    }

    @Test
    void invalidPassword() {
        var userInput = new UserInput("myName", "");
        var userEither = User.of(userInput);

        assertTrue(userEither.isLeft());
        assertEquals(INVALID_FIELD, userEither.getLeft().getErrorType());
    }
}
