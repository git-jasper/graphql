package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.model.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MotorcycleRepository extends ReactiveCrudRepository<MotorcycleEntity, Long> {
    Mono<Integer> removeById(Long id);

    Flux<MotorcycleEntity> findAllByBrand(Brand brand);
}
