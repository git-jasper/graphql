package com.jpr.maintenance.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.control.Either;
import org.springframework.dao.DataAccessException;

import java.util.function.Function;

import static com.jpr.maintenance.validation.errors.InputValidationError.DATA_PERSISTENCE_ERROR;

public class GraphQLUtils {
    public static <T> Either<GraphQLError, T> saveEntity(T entity, Function<T, T> persistenceFun, DataFetchingEnvironment environment) {
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
}
