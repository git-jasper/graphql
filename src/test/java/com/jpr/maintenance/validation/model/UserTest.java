package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.validation.errors.InputValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;

public class UserTest {

    @Test
    void createUserOk() {
        String name = "myName";
        String password = "myPassword";
        var userInput = new UserInput(name, password);
        var userMono = User.of(userInput);

        StepVerifier
            .create(userMono)
            .expectNextMatches(u -> u.username().equals(name) && u.plainPassword().equals(password))
            .expectComplete()
            .verify();
    }

    @Test
    void invalidName() {
        var userInput = new UserInput("", "myPassword");
        var userMono = User.of(userInput);

        StepVerifier
            .create(userMono)
            .expectErrorMatches(t -> t instanceof InputValidationException && ((InputValidationException) t).getError().equals(INVALID_FIELD))
            .verify();
    }

    @Test
    void invalidPassword() {
        var userInput = new UserInput("myName", "");
        var userMono = User.of(userInput);

        StepVerifier
            .create(userMono)
            .expectErrorMatches(t -> t instanceof InputValidationException && ((InputValidationException) t).getError().equals(INVALID_FIELD))
            .verify();
    }
}
