package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.validation.ValidationService;
import graphql.GraphQLError;
import io.vavr.control.Either;

public class User {
    private final String username;
    private final String plainPassword;

    private User(String username, String plainPassword) {
        this.username = username;
        this.plainPassword = plainPassword;
    }

    public String username() {
        return username;
    }

    public String plainPassword() {
        return plainPassword;
    }

    public static Either<GraphQLError, User> of(UserInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> Either.right(
                new User(
                    input.username(),
                    input.password()
                )
            ));
    }
}
