package com.jpr.maintenance.validation.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.validation.ValidationService;
import reactor.core.publisher.Mono;

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

    public static Mono<User> of(UserInput input) {
        return ValidationService.validate(input)
            .map(i ->
                new User(
                    i.username(),
                    i.password()
                )
            );
    }
}
