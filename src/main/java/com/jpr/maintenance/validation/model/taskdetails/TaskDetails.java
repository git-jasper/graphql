package com.jpr.maintenance.validation.model.taskdetails;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;
@RequiredArgsConstructor
@Getter
public class TaskDetails {

    private static final Pattern descriptionWhitelist = Pattern.compile("[a-zA-Z ]{1,100}");
    private static final Pattern integerWhitelist = Pattern.compile("\\d{1,10}");

    private final String description;
    private final Integer interval_km;
    private final Integer interval_months;

    public static Either<GraphQLError, TaskDetails> of(DataFetchingEnvironment environment) {
        final String description = environment.getArgument("description");
        if (!descriptionWhitelist.matcher(description).matches()) {
            return Either.left(
                    GraphqlErrorBuilder.newError()
                            .errorType(InputValidationError.INVALID_DESCRIPTION)
                            .message("The description must consist of only letters and spaces, between 1 and 100 characters long.")
                            .build()
            );
        }
        Integer interval_km = environment.getArgument("interval_km");
        Integer interval_months = environment.getArgument("interval_months");
        return Either.right(new TaskDetails(
            description,
            interval_km,
            interval_months
        ));
    }
}
