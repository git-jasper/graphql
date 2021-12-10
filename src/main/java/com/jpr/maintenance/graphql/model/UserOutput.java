package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.database.model.UserEntity;

public record UserOutput(Long id, String username) {
    public static UserOutput of(UserEntity entity) {
        return new UserOutput(entity.getId(), entity.getUsername());
    }
}
