package com.jpr.maintenance.reflection;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.vavr.control.Either;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class ReflectiveConverter {

    public static <T> Either<GraphQLError, T> toObject(Map<String, Object> map, Class<T> clazz) {
        try {
            Class<?>[] fieldTypes = Arrays.stream(clazz.getDeclaredFields()).map(Field::getType).toArray(Class<?>[]::new);
            Object[] arguments = Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).map(map::get).toArray(Object[]::new);
            return Either.right(clazz.getConstructor(fieldTypes).newInstance(arguments));
        } catch (Exception e) {
            return Either.left(GraphqlErrorBuilder.newError()
                .errorType(InputValidationError.FAILED_TO_INSTANTIATE_OBJECT)
                .message("Failed to instantiate object from input")
                .build()
            );
        }
    }
}
