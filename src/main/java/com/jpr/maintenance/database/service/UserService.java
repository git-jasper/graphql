package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepositoryEntity;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.model.User;
import graphql.GraphQLError;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryEntity userRepository;

    public Mono<Either<GraphQLError, UserOutput>> findByUser(final User user) {
        return userRepository.findByUserName(user.username())
            .map(u -> u.verify(user))
            .map(e -> e.flatMap(UserOutput::of));
    }

    public Mono<UserOutput> save(final UserEntity user) {
        return userRepository.saveUser(user)
            .map(UserOutput::of)
            .map(Either::get);
    }

    public Mono<Boolean> deleteByUser(final User user) {
        return userRepository.removeByUsernameAndPassword(user.username(), user.plainPassword())
            .map(i -> i == 1);
    }
}
