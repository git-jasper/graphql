package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.model.Password;
import com.jpr.maintenance.validation.errors.InputValidationException;
import com.jpr.maintenance.validation.model.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private static final String USER_NAME = "USER";
    private static final Mono<User> USER = User.of(new UserInput(USER_NAME, "secret"));
    private static final Mono<UserEntity> ENTITY = USER.flatMap(UserServiceTest::createEntity);

    private final UserRepository repository = mock(UserRepository.class);
    private final UserService service = new UserService(repository);

    @Test
    void findByUserOk() {
        when(repository.findByUserName(USER_NAME)).thenReturn(ENTITY);
        Mono<UserOutput> result = USER.flatMap(service::findByUser);

        StepVerifier
            .create(result)
            .expectNextMatches(u -> u != null && u.username().equals(USER_NAME))
            .expectComplete()
            .verify();
    }

    @Test
    void findByUserError() {
        Mono<User> other = User.of(new UserInput(USER_NAME, "wrong"));

        when(repository.findByUserName(USER_NAME)).thenReturn(ENTITY);
        Mono<UserOutput> result = other.flatMap(service::findByUser);

        StepVerifier
            .create(result)
            .expectError(InputValidationException.class)
            .verify();
    }

    @Test
    void save() {
        when(repository.saveUser(any(UserEntity.class))).thenReturn(ENTITY);
        Mono<UserOutput> result = ENTITY.flatMap(service::save);

        StepVerifier
            .create(result)
            .expectNextMatches(u -> u != null && u.id().equals(1L) && u.username().equals(USER_NAME))
            .expectComplete()
            .verify();
    }

    @Test
    void deleteByIdTrue() {
        when(repository.removeById(1L)).thenReturn(Mono.just(1));
        Mono<Boolean> result = service.deleteById(1L);

        StepVerifier
            .create(result)
            .expectNextMatches(b -> b)
            .expectComplete()
            .verify();
    }

    @Test
    void deleteByIdFalse() {
        when(repository.removeById(0L)).thenReturn(Mono.just(0));
        Mono<Boolean> result = service.deleteById(0L);

        StepVerifier
            .create(result)
            .expectNextMatches(b -> !b)
            .expectComplete()
            .verify();
    }

    private static Mono<UserEntity> createEntity(final User user) {
        return Password.of(user.plainPassword(), "salt")
            .map(p -> UserEntity.builder()
                .id(1L)
                .username(user.username())
                .password(p.password())
                .salt(p.salt())
                .build()
            );
    }
}