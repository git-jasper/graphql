package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserOutput> findByUser(final User user) {
        return userRepository.findByUserName(user.username())
            .flatMap(u -> u.verify(user))
            .map(UserOutput::of);
    }

    public Mono<UserOutput> save(final UserEntity user) {
        return userRepository.saveUser(user)
            .map(UserOutput::of);
    }

    public Mono<Boolean> deleteById(final Long id) {
        return userRepository.removeById(id).map(i -> i == 1);
    }
}
