package com.jpr.maintenance.database.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.validation.model.User;
import graphql.GraphQLError;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static com.jpr.maintenance.model.Password.of;
import static com.jpr.maintenance.validation.errors.InputValidationError.USER_ACCESS_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityTest {

    private final UserEntity entity = UserEntity.builder()
        .password(of("secret", "salt").get().password())
        .salt("salt")
        .build();

    @Test
    void verifyOk() {
        User user = User.of(new UserInput("user", "secret")).get();
        Either<GraphQLError, UserEntity> result = entity.verify(user);

        assertTrue(result.isRight());
        assertEquals(entity, result.get());
    }

    @Test
    void verifyError() {
        User user = User.of(new UserInput("user", "wrong")).get();
        Either<GraphQLError, UserEntity> result = entity.verify(user);

        assertTrue(result.isLeft());
        assertEquals(USER_ACCESS_ERROR, result.getLeft().getErrorType());
    }
}