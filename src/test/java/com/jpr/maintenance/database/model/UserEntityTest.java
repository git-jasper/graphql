package com.jpr.maintenance.database.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.model.Password;
import com.jpr.maintenance.validation.errors.InputValidationException;
import com.jpr.maintenance.validation.model.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UserEntityTest {
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "secret";
    private static final String SALT = "salt";
    private final Mono<UserEntity> entity = Password.of(PASSWORD, SALT)
        .map(p -> UserEntity.builder()
            .id(1L)
            .username(USER_NAME)
            .password(p.password())
            .salt(p.salt())
            .build()
        );

    @Test
    void verifyOk() {
        Mono<User> user = User.of(new UserInput(USER_NAME, PASSWORD));
        Mono<UserEntity> result = entity
            .flatMap(e -> user
                .flatMap(e::verify)
            );

        StepVerifier
            .create(result.zipWith(entity))
            .expectNextMatches(t -> t.getT1().equals(t.getT2()))
            .expectComplete()
            .verify();
    }

    @Test
    void verifyError() {
        Mono<User> user = User.of(new UserInput("user", "wrong"));
        Mono<UserEntity> result = entity
            .flatMap(e -> user
                .flatMap(e::verify)
            );

        StepVerifier
            .create(result)
            .expectError(InputValidationException.class)
            .verify();
    }
}