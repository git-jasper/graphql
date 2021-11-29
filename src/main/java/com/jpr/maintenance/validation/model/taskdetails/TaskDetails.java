package com.jpr.maintenance.validation.model.taskdetails;

import com.jpr.maintenance.graphql.model.TaskDetailsInput;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;

@Getter
@RequiredArgsConstructor
public class TaskDetails {

    private static final Pattern descriptionWhitelist = Pattern.compile("[a-zA-Z ]{1,100}");

    private final String description;
    private final Integer interval_km;
    private final Integer interval_months;

    public static Either<GraphQLError, TaskDetails> of(TaskDetailsInput taskDetailsInput) {
        final String description = taskDetailsInput.getDescription();
        if (!descriptionWhitelist.matcher(description).matches()) {
            return Either.left(
                GraphqlErrorBuilder.newError()
                    .errorType(INVALID_FIELD)
                    .message(INVALID_FIELD.getErrorMessage("description"))
                    .build()
            );
        }
        return Either.right(new TaskDetails(
            description,
            taskDetailsInput.getInterval_km(),
            taskDetailsInput.getInterval_months()
        ));
    }
}
