package com.jpr.maintenance.graphql.handler;

import graphql.execution.DataFetcherResult;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.jpr.maintenance.graphql.GraphQLUtils.createError;
import static com.jpr.maintenance.validation.errors.InputValidationError.UNEXPECTED_ERROR;

public class ThrowableHandlerProvider {
    private static <T> Handler<Throwable, DataFetcherResult<T>> getHandlerChain() {
        return new InputValidationExceptionHandler<T>()
            .orElse(new DataAccessExceptionHandler<>());
    }

    public static <T> Function<Throwable, Mono<DataFetcherResult<T>>> handlerFunction() {
        return t -> Mono.just(
            ThrowableHandlerProvider.<T>getHandlerChain()
                .handle(t, ex -> DataFetcherResult.<T>newResult()
                    .error(createError(UNEXPECTED_ERROR, ex.getClass().getSimpleName()))
                    .build()
                )
        );
    }
}
