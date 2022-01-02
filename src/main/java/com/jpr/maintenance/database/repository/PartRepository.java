package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.PartEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends ReactiveCrudRepository<PartEntity, Long> {
}
