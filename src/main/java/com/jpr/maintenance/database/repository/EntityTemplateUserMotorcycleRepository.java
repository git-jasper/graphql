package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
import reactor.core.publisher.Mono;

public interface EntityTemplateUserMotorcycleRepository {
    Mono<UserMotorcycleEntity> saveUserMotorcycle(UserMotorcycleInput input);
}
