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
    public static <T> Either<GraphQLError, CompletableFuture<T>> saveEntity(T entity, Function<T, Mono<T>> persistenceFun, DataFetchingEnvironment environment) {
        try {
            return Either.right(persistenceFun.apply(entity).toFuture());
        } catch (DataAccessException e) {
            return Either.left(
                GraphqlErrorBuilder
                    .newError(environment)
                    .errorType(DATA_PERSISTENCE_ERROR)
                    .build()
            );
        }
    }

    public static <T> Function<T, DataFetcherResult<T>> successFun() {
        return r -> DataFetcherResult.<T>newResult().data(r).build();
    }

    public static <T> Function<GraphQLError, DataFetcherResult<T>> errorFun() {
        return l -> DataFetcherResult.<T>newResult().error(l).build();
    }

    public static <T> Either<GraphQLError, T> createLeft(InputValidationError classification, String errorArg) {
        return Either.left(GraphqlErrorBuilder
            .newError()
            .message(classification.getErrorMessage(errorArg))
            .errorType(classification)
            .build());
    }
}
