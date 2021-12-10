package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepositoryEntity;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryEntity userRepository;

    public Mono<UserEntity> findByUser(User user) {
        Mono<UserEntity> userEntity = userRepository.findByUserName(user.username());
        return userEntity;
    }

    public Mono<UserOutput> save(UserEntity user) {
        return userRepository.saveUser(user)
            .map(UserOutput::of);
    }

    public Mono<Boolean> deleteByUser(User user) {
        return userRepository.removeByUsernameAndPassword(user.username(), user.password())
            .map(i -> i == 1);
    }
}
