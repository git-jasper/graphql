package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public record UserOutput(Long id, String username, List<UserMotorcycleEntity> motorcycles) {
    public static UserOutput of(UserEntity entity) {
        return new UserOutput(entity.getId(), entity.getUsername(), entity.getMotorcycles());
    }
}
