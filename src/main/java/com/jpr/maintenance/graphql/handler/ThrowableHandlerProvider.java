package com.jpr.maintenance.graphql.handler;

import com.jpr.maintenance.validation.errors.InputValidationException;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;
import org.springframework.dao.DataAccessException;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.jpr.maintenance.graphql.GraphQLUtils.createError;
import static com.jpr.maintenance.validation.errors.InputValidationError.DATA_ACCESS_ERROR;
import static com.jpr.maintenance.validation.errors.InputValidationError.UNEXPECTED_ERROR;

public class ThrowableHandlerProvider {
    public static <T> Function<Throwable, Mono<DataFetcherResult<T>>> handlerFunction() {
        return t -> Mono.just(
            ThrowableHandlerProvider.<T>getHandlerChain()
                .handle(t, ex -> DataFetcherResult.<T>newResult()
                    .error(createError(UNEXPECTED_ERROR, ex.getClass().getSimpleName()))
                    .build()
                )
        );
    }

    private static <T> Handler<Throwable, DataFetcherResult<T>> getHandlerChain() {
        return ThrowableHandlerProvider.<T>inputValidationExceptionHandler()
            .orElse(dataAccessExceptionHandler());
    }

    private static <T> Handler<Throwable, DataFetcherResult<T>> createHandler(Predicate<Throwable> predicate, Function<Throwable, DataFetcherResult<T>> resultFun) {
        return t -> predicate.test(t) ? Optional.of(resultFun.apply(t)) : Optional.empty();
    }

    private static <T> Handler<Throwable, DataFetcherResult<T>> dataAccessExceptionHandler() {
        Predicate<Throwable> predicate = t -> t instanceof DataAccessException;

        return createHandler(predicate, t -> DataFetcherResult.<T>newResult()
            .error(createError(DATA_ACCESS_ERROR, ""))
            .build()
        );
    }

    private static <T> Handler<Throwable, DataFetcherResult<T>> inputValidationExceptionHandler() {
        Predicate<Throwable> predicate = t -> t instanceof InputValidationException;

        return createHandler(predicate, t -> {
            InputValidationException ex = (InputValidationException) t;
            return DataFetcherResult.<T>newResult()
                .error(GraphqlErrorBuilder
                    .newError()
                    .errorType(ex.getError())
                    .message(ex.getMessage())
                    .build())
                .build();
        });
    }
}
