package com.jpr.maintenance.graphql;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.control.Either;
import org.springframework.dao.DataAccessException;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.jpr.maintenance.validation.errors.InputValidationError.DATA_PERSISTENCE_ERROR;

public class GraphQLUtils {
    public static <T> Either<GraphQLError, Mono<T>> saveEntity(T entity, Function<T, Mono<T>> persistenceFun, DataFetchingEnvironment environment) {
        try {
            return Either.right(persistenceFun.apply(entity));
        } catch (DataAccessException e) {
            return Either.left(
                GraphqlErrorBuilder
                    .newError(environment)
                    .errorType(DATA_PERSISTENCE_ERROR)
                    .build()
            );
        }
    }

    public static <T> Function<Mono<T>, CompletableFuture<DataFetcherResult<T>>> successFun() {
        return r -> r.map(v -> DataFetcherResult.<T>newResult().data(v).build()).toFuture();
    }

    public static <T> Function<GraphQLError, CompletableFuture<DataFetcherResult<T>>> errorFun() {
        return l -> CompletableFuture.supplyAsync(() -> DataFetcherResult.<T>newResult().error(l).build());
    }

    public static <T> Either<GraphQLError, T> createLeft(InputValidationError classification, String errorArg) {
        return Either.left(GraphqlErrorBuilder
            .newError()
            .message(classification.getErrorMessage(errorArg))
            .errorType(classification)
            .build());
    }
}
