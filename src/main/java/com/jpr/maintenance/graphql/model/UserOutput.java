package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.database.model.UserEntity;
import graphql.GraphQLError;
import io.vavr.control.Either;

public record UserOutput(Long id, String username) {
    public static Either<GraphQLError, UserOutput> of(UserEntity entity) {
        return Either.right(new UserOutput(entity.getId(), entity.getUsername()));
    }
}
