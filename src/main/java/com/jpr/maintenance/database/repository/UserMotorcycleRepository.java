package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMotorcycleRepository extends ReactiveCrudRepository<UserMotorcycleEntity, Long>, EntityTemplateUserMotorcycleRepository {
}
