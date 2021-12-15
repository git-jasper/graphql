package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.model.User;
import graphql.GraphQLError;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static com.jpr.maintenance.model.Password.of;
import static com.jpr.maintenance.validation.errors.InputValidationError.USER_ACCESS_ERROR;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private  static final User USER = User.of(new UserInput("USER", "secret")).get();
    private  static final UserEntity ENTITY = createEntity(USER);

    private final UserRepository repository = mock(UserRepository.class);
    private final UserService service = new UserService(repository);

    @Test
    void findByUserOk() {
        when(repository.findByUserName(USER.username())).thenReturn(Mono.just(ENTITY));
        Either<GraphQLError, UserOutput> result = service.findByUser(USER).block();

        assertNotNull(result);
        assertTrue(result.isRight());
        assertEquals(USER.username(), result.get().username());
    }

    @Test
    void findByUserError() {
        User other = User.of(new UserInput("USER", "wrong")).get();

        when(repository.findByUserName(USER.username())).thenReturn(Mono.just(ENTITY));
        Either<GraphQLError, UserOutput> result = service.findByUser(other).block();

        assertNotNull(result);
        assertTrue(result.isLeft());
        assertEquals(USER_ACCESS_ERROR, result.getLeft().getErrorType());
    }

    @Test
    void save() {
        when(repository.saveUser(ENTITY)).thenReturn(Mono.just(ENTITY));
        UserOutput result = service.save(ENTITY).block();

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("USER", result.username());
    }

    @Test
    void deleteByIdTrue() {
        when(repository.removeById(1L)).thenReturn(Mono.just(1));
        Boolean result = service.deleteById(1L).block();

        assertEquals(TRUE, result);
    }

    @Test
    void deleteByIdFalse() {
        when(repository.removeById(0L)).thenReturn(Mono.just(0));
        Boolean result = service.deleteById(0L).block();

        assertEquals(FALSE, result);
    }

    private static UserEntity createEntity(final User user) {
        return UserEntity.builder()
            .id(1L)
            .username(user.username())
            .password(of(user.plainPassword(), "salt").get().password())
            .salt("salt")
            .build();
    }
}