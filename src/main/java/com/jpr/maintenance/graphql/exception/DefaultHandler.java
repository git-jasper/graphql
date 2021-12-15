package com.jpr.maintenance.graphql.exception;

import graphql.execution.DataFetcherResult;

import static com.jpr.maintenance.graphql.GraphQLUtils.createError;
import static com.jpr.maintenance.validation.errors.InputValidationError.UNEXPECTED_ERROR;

public class DefaultHandler extends ThrowableHandler {

    @Override
    public <T> DataFetcherResult<T> handle(Throwable throwable) {
        return DataFetcherResult.<T>newResult()
            .error(createError(UNEXPECTED_ERROR, throwable.getClass().getSimpleName()))
            .build();
    }
}
