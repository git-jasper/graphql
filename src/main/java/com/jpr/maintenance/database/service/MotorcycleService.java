package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.repository.MotorcycleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;

    public Mono<MotorcycleEntity> findById(Long id) {
        return motorcycleRepository.findById(id);
    }

    public Mono<MotorcycleEntity> save(MotorcycleEntity motorcycle) {
        return motorcycleRepository.save(motorcycle);
    }

    public Mono<Boolean> deleteById(Long id) {
        return motorcycleRepository.removeById(id)
            .map(i -> i == 1);
    }
}
