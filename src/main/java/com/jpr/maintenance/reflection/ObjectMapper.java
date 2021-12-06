package com.jpr.maintenance.reflection;

import com.jpr.maintenance.graphql.GraphQLUtils;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.jpr.maintenance.graphql.GraphQLUtils.createLeft;
import static com.jpr.maintenance.validation.errors.InputValidationError.*;
import static java.util.stream.Collectors.toList;

@Slf4j
public class ObjectMapper {

    public static <T> Either<GraphQLError, T> toObject(Map<String, Object> arguments, Class<T> clazz) {
        List<FieldInfo> fields = getFields(clazz);
        Class<?>[] parameterTypes = getParameterTypes(fields);
        return arrayRight(fields.stream()
            .map(newArgumentFun(arguments))
            .collect(toList()))
            .fold(
                errorFun(),
                newInstanceFun(clazz, parameterTypes)
            );
    }

    private static <T> List<FieldInfo> getFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .map(newFieldInfoFun())
            .collect(toList());
    }

    private static Class<?>[] getParameterTypes(List<FieldInfo> fields) {
        return fields.stream().map(FieldInfo::getType).toArray(Class<?>[]::new);
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

    private static Function<Field, FieldInfo> newFieldInfoFun() {
        return field -> new FieldInfo(field.getName(), field.getType(), getGenericType(field.getGenericType()));
    }

    private static Function<FieldInfo, Either<GraphQLError, ?>> newArgumentFun(Map<String, Object> arguments) {
        return field -> {
            Object object = arguments.get(field.getName());
            Class<?> clazz = (Class<?>) field.getGenericType();
            if (clazz.isInstance(object)) {
                return Either.right(clazz.cast(object));
            } else if (clazz.isEnum()) {
                return createEnumInstance((String) object, field);
            } else if (object instanceof Map) {
                return toObject(((Map<String, Object>) object), clazz);
            } else if (object instanceof List) {
                return listRight(((List<?>) object).stream()
                    .map(obj -> toObject(((Map<String, Object>) obj), clazz))
                    .collect(toList()));
            } else {
                return createLeft(FAILED_TO_RESOLVE_FIELD, field.getName());
            }
        };
    }

    private static <T extends Enum<T>> Either<GraphQLError, T> createEnumInstance(String name, FieldInfo fieldInfo) {
        return name != null
            ? Either.right(Enum.valueOf((Class<T>) fieldInfo.getType(), name))
            : createLeft(NULL_VALUE, fieldInfo.getName());
    }

    private static <T> Function<GraphQLError, Either<GraphQLError, T>> errorFun() {
        return Either::left;
    }

    private static <T> Function<Object[], Either<GraphQLError, T>> newInstanceFun(Class<T> clazz, Class<?>[] constructorTypes) {
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
