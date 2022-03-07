package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.graphql.model.FindUserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<UserOutput> findByUserName(final FindUserInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> userRepository.findByUserName(i.username()))
            .map(UserOutput::of);
    }

    public Mono<UserOutput> save(final UserEntity user) {
        return userRepository.saveUser(user)
            .map(UserOutput::of);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<Boolean> deleteById(final Long id) {
        return userRepository.removeById(id).map(i -> i == 1);
    }
}
