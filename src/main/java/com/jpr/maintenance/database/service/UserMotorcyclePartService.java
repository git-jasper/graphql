package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserMotorcyclePartEntity;
import com.jpr.maintenance.database.repository.UserMotorcyclePartRepository;
import com.jpr.maintenance.graphql.model.UserMotorcyclePartInput;
import com.jpr.maintenance.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserMotorcyclePartService {
    private final UserMotorcyclePartRepository userMotorcyclePartRepository;

    public Mono<UserMotorcyclePartEntity> save(final UserMotorcyclePartInput input) {
        return ValidationService.validate(input)
            .flatMap(userMotorcyclePartRepository::saveUserMotorcyclePart);
    }
}
