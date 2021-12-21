package com.jpr.maintenance.graphql.handler;

import graphql.execution.DataFetcherResult;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static com.jpr.maintenance.graphql.GraphQLUtils.createError;
import static com.jpr.maintenance.validation.errors.InputValidationError.DATA_ACCESS_ERROR;

public class DataAccessExceptionHandler<T> implements Handler<Throwable, DataFetcherResult<T>> {

    @Override
    public Optional<DataFetcherResult<T>> handleMaybe(Throwable throwable) {
        if (throwable instanceof DataAccessException) {
            return Optional.of(DataFetcherResult.<T>newResult()
                .error(createError(DATA_ACCESS_ERROR, ""))
                .build());
        }
        return Optional.empty();
    }
}
