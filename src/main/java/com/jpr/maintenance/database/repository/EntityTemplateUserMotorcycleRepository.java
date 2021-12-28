package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import reactor.core.publisher.Mono;

public interface EntityTemplateUserMotorcycleRepository {
    Mono<UserMotorcycleEntity> saveUserMotorcycle(Long userId, Long motorcycleId, String color);
}
