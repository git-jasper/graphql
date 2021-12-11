package com.jpr.maintenance.model;

import graphql.GraphQLError;
import io.vavr.control.Either;

public class Password {

    private final String password;
    private final String salt;

    private Password(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    public String password() {
        return password;
    }

    public String salt() {
        return salt;
    }

    public static Either<GraphQLError, Password> of(String plainPassword) {
        return Either.right(new Password("#"+plainPassword+":salt", "salt"));
    }

    public static Either<GraphQLError, Password> of(String plainPassword, String salt) {
        return Either.right(new Password("#"+plainPassword+":"+salt, salt));
    }

}
