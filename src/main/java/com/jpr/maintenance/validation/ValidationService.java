package com.jpr.maintenance.validation;

import com.jpr.maintenance.validation.errors.InputValidationException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.vavr.control.Either;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ValidationService {
    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> Mono<T> validateReactor(T arg) {
        var validationResult = VALIDATOR.validate(arg);

        return validationResult.isEmpty() ? Mono.just(arg) : Mono.error(
            new InputValidationException(
                INVALID_FIELD,
                validationResult
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(joining(" ")),
                validationResult
                    .stream()
                    .map(r -> String.format("Invalid field: %s, constraint: %s, invalid value: %s", r.getPropertyPath().toString(), r.getMessage(), r.getInvalidValue()))
                    .collect(toList())
            )
        );
    }
    
    public static <T> Either<GraphQLError, T> validate(T arg) {
        var validationResult = VALIDATOR.validate(arg);
        return validationResult.isEmpty() ? Either.right(arg) : Either.left(
            GraphqlErrorBuilder
                .newError()
                .errorType(INVALID_FIELD)
                .message(validationResult
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(joining(" "))
                )
                .path(validationResult
                    .stream()
                    .map(r -> String.format("Invalid field: %s, constraint: %s, invalid value: %s", r.getPropertyPath().toString(), r.getMessage(), r.getInvalidValue()))
                    .collect(toList())
                )
                .build()
        );
    }
}
