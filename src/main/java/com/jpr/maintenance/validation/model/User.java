package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.validation.ValidationService;
import graphql.GraphQLError;
import io.vavr.control.Either;

public record User(String username, String password) {
    public static Either<GraphQLError, User> of(UserInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> Either.right(
                new User(
                    input.username(),
                    input.password() // TODO hash+salt
                )
            ));
    }
}
