package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.repository.MotorcycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public Mono<Void> deleteById(Long id) {
        return motorcycleRepository.deleteById(id);
    }
}
