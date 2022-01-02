package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.database.repository.UserMotorcycleRepository;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
import com.jpr.maintenance.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserMotorcycleService {
    private final UserMotorcycleRepository userMotorcycleRepository;

    public Mono<UserMotorcycleEntity> save(final UserMotorcycleInput input) {
        return ValidationService.validate(input)
            .flatMap(userMotorcycleRepository::saveUserMotorcycle);
    }
}
