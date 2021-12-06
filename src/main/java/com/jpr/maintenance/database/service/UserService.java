package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.validation.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserEntity> findByUser(User user) {
        return userRepository.findByUserNameAndPassword(user.username(), user.password());
    }

    public Mono<Boolean> save(UserEntity user) {
        return userRepository.saveUser(user.getUsername(), user.getPassword())
            .map(i -> i == 1);
    }

    public Mono<Boolean> deleteByUser(User user) {
        return userRepository.removeByUsernameAndPassword(user.username(), user.password())
            .map(i -> i == 1);
    }
}
