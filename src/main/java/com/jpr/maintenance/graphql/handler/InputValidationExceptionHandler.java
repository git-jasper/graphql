package com.jpr.maintenance.graphql.handler;

import com.jpr.maintenance.validation.errors.InputValidationException;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;

import java.util.Optional;

public class InputValidationExceptionHandler<T> implements Handler<Throwable, DataFetcherResult<T>> {

    @Override
    public Optional<DataFetcherResult<T>> handleMaybe(Throwable throwable) {
        if (throwable instanceof InputValidationException ex) {
            return Optional.of(DataFetcherResult.<T>newResult()
                .error(GraphqlErrorBuilder
                    .newError()
                    .errorType(ex.getError())
                    .message(ex.getMessage())
                    .path(ex.getPath())
                    .build())
                .build());
        }
        return Optional.empty();
    }
}
