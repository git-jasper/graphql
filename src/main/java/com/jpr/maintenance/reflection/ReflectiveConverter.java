package com.jpr.maintenance.reflection;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.jpr.maintenance.validation.errors.InputValidationError.FAILED_TO_INSTANTIATE_OBJECT;
import static com.jpr.maintenance.validation.errors.InputValidationError.FAILED_TO_RESOLVE_FIELD;
import static java.util.stream.Collectors.toList;

public class ReflectiveConverter {

    public static <T> Either<GraphQLError, T> toObject(Map<String, Object> map, Class<T> clazz) {
        List<FieldInfo> fields = Arrays.stream(clazz.getDeclaredFields())
            .map(getFieldFun())
            .collect(toList());
        Class<?>[] constructorTypes = fields.stream().map(FieldInfo::getType).toArray(Class<?>[]::new);
        return arrayRight(fields.stream()
            .map(resolveFieldFun(map))
            .collect(toList()))
            .fold(
                Either::left,
                constructInstance(clazz, constructorTypes)
            );
    }

    private static Function<Field, FieldInfo> getFieldFun() {
        return field -> new FieldInfo(field.getName(), field.getType(), getGenericType(field.getGenericType()));
    }

    private static Type getGenericType(Type type) {
        return type instanceof ParameterizedType ? ((ParameterizedType) type).getActualTypeArguments()[0] : type;
    }

    private static <L, R> Either<L, R[]> arrayRight(Iterable<? extends Either<? extends L, ? extends R>> eithers) {
        return listRight(eithers).map(r -> (R[]) r.toArray());
    }

    private static <L, R> Either<L, List<R>> listRight(Iterable<? extends Either<? extends L, ? extends R>> eithers) {
        List<R> rightValues = new ArrayList<>();
        for (Either<? extends L, ? extends R> either : eithers) {
            if (either.isRight()) {
                rightValues.add(either.get());
            } else {
                return Either.left(either.getLeft());
            }
        }
        return Either.right(rightValues);
    }

    private static Function<FieldInfo, Either<GraphQLError, ?>> resolveFieldFun(Map<String, Object> map) {
        return field -> {
            Object object = map.get(field.getName());
            Class<?> clazz = (Class<?>) field.getGenericType();
            if (clazz.isInstance(object)) {
                return Either.right(clazz.cast(object));
            } else if (object instanceof Map) {
                return toObject(((Map<String, Object>) object), clazz);
            } else if (object instanceof List) {
                return listRight(((List<?>) object).stream()
                    .map(obj -> toObject(((Map<String, Object>) obj), clazz))
                    .collect(toList()));
            } else {
                return Either.left(GraphqlErrorBuilder.newError()
                    .errorType(FAILED_TO_RESOLVE_FIELD)
                    .message(FAILED_TO_RESOLVE_FIELD.getErrorMessage(field.getName()))
                    .build()
                );
            }
        };
    }

    private static <T> Function<Object[], Either<GraphQLError, T>> constructInstance(Class<T> clazz, Class<?>[] constructorTypes) {
        return r -> {
            try {
                return Either.right(clazz.getConstructor(constructorTypes).newInstance(r));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return Either.left(GraphqlErrorBuilder.newError()
                    .errorType(FAILED_TO_INSTANTIATE_OBJECT)
                    .message(FAILED_TO_INSTANTIATE_OBJECT.getErrorMessage(clazz.getSimpleName()))
                    .build()
                );
            }
        };
    }

    @Getter
    @RequiredArgsConstructor
    static class FieldInfo {
        private final String name;
        private final Type type;
        private final Type genericType;
    }
}
