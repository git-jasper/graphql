package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.repository.PartRepository;
import com.jpr.maintenance.graphql.model.PartInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;

    public Mono<PartEntity> save(PartInput input) {
        return PartEntity.of(input)
            .flatMap(partRepository::save);
    }
}
