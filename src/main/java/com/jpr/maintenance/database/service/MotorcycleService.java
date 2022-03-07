package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.repository.MotorcycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<MotorcycleEntity> findById(Long id) {
        return motorcycleRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<MotorcycleEntity> save(MotorcycleEntity motorcycle) {
        return motorcycleRepository.save(motorcycle);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<Boolean> deleteById(Long id) {
        return motorcycleRepository.removeById(id)
            .map(i -> i == 1);
    }
}
