package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.testutil.DummyUserRepository;
import com.jpr.maintenance.validation.model.User;
import graphql.GraphQLError;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static com.jpr.maintenance.validation.errors.InputValidationError.USER_ACCESS_ERROR;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private final UserRepository repository = new DummyUserRepository();
    private final UserService service = new UserService(repository);

    @Test
    void findByUserOk() {
        User user = User.of(new UserInput("user", "secret")).get();
        Either<GraphQLError, UserOutput> result = service.findByUser(user).block();

        assertNotNull(result);
        assertTrue(result.isRight());
        assertEquals(user.username(), result.get().username());
    }

    @Test
    void findByUserError() {
        User user = User.of(new UserInput("user", "wrong")).get();
        Either<GraphQLError, UserOutput> result = service.findByUser(user).block();

        assertNotNull(result);
        assertTrue(result.isLeft());
        assertEquals(USER_ACCESS_ERROR, result.getLeft().getErrorType());
    }

    @Test
    void save() {
        UserEntity entity = UserEntity.builder()
            .id(1L)
            .username("user")
            .build();
        UserOutput result = service.save(entity).block();

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("user", result.username());
    }

    @Test
    void deleteByIdTrue() {
        Boolean result = service.deleteById(1L).block();

        assertEquals(TRUE, result);
    }

    @Test
    void deleteByIdFalse() {
        Boolean result = service.deleteById(0L).block();

        assertEquals(FALSE, result);
    }
}