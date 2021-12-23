package com.jpr.maintenance.graphql.exception;

import com.jpr.maintenance.validation.errors.InputValidationError;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import reactor.test.StepVerifier;

class ThrowableHandlerProviderTest {

    @Test
    void expectDataAccessExceptionToBeHandled() {
        var handlerFun = ThrowableHandlerProvider.<Boolean>handlerFunction();

        DataAccessException ex = new DuplicateKeyException("");

        StepVerifier
            .create(handlerFun.apply(ex))
            .expectNextMatches(r -> r.hasErrors() && r.getErrors().get(0).getMessage().equals(InputValidationError.DATA_ACCESS_ERROR.getErrorMessage("")))
            .expectComplete()
            .verify();
    }
}