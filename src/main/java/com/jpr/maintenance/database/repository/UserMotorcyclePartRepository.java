package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserMotorcyclePartEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserMotorcyclePartRepository extends ReactiveCrudRepository<UserMotorcyclePartEntity, Long>, EntityTemplateUserMotorcyclePartRepository {
}
