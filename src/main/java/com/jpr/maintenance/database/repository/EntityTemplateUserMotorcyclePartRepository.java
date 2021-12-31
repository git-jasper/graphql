package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserMotorcyclePartEntity;
import com.jpr.maintenance.graphql.model.UserMotorcyclePartInput;
import reactor.core.publisher.Mono;

public interface EntityTemplateUserMotorcyclePartRepository {
    Mono<UserMotorcyclePartEntity> saveUserMotorcyclePart(UserMotorcyclePartInput input);
}
