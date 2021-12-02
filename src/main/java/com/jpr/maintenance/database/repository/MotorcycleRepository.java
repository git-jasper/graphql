package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorcycleRepository extends ReactiveCrudRepository<MotorcycleEntity, Long> {
}
