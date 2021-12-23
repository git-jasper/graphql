package com.jpr.maintenance.graphql;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class GraphQLUtils {

    public static <T> Mono<T> saveEntity(T entity, Function<T, Mono<T>> persistenceFun) {
        return persistenceFun.apply(entity);
    }

    public static <T, U> Mono<T> serviceCall(U input, Function<U, Mono<T>> serviceFun) {
        return serviceFun.apply(input);
    }

    public static <T> Function<T, DataFetcherResult<T>> successFun() {
        return r -> DataFetcherResult.<T>newResult().data(r).build();
    }

    public static GraphQLError createError(InputValidationError classification, String errorArg) {
        return GraphqlErrorBuilder
            .newError()
            .message(classification.getErrorMessage(errorArg))
            .errorType(classification)
            .build();
    }
}
