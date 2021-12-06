package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.UserInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void createUserOk() {
        var userInput = new UserInput("myName", "myPassword");
        var userEither = User.of(userInput);

        Assertions.assertTrue(userEither.isRight());
        var user = userEither.get();
        Assertions.assertEquals("myName", user.username());
        Assertions.assertEquals("myPassword", user.password());
    }
}
