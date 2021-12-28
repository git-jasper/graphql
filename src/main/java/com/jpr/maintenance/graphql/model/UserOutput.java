package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.database.model.UserEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record UserOutput(Long id, String username) {
    public static UserOutput of(UserEntity entity) {
        return new UserOutput(entity.getId(), entity.getUsername());
    }
}
