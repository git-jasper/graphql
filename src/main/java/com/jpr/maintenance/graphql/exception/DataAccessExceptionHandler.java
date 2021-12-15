package com.jpr.maintenance.graphql.exception;

import graphql.execution.DataFetcherResult;
import org.springframework.dao.DataAccessException;

import static com.jpr.maintenance.graphql.GraphQLUtils.createError;
import static com.jpr.maintenance.validation.errors.InputValidationError.DATA_ACCESS_ERROR;

public class DataAccessExceptionHandler extends ThrowableHandler {

    @Override
    public <T> DataFetcherResult<T> handle(Throwable throwable) {
        if (throwable instanceof DataAccessException) {
            return DataFetcherResult.<T>newResult()
                .error(createError(DATA_ACCESS_ERROR, ""))
                .build();
        }
        return next.handle(throwable);
    }
}
