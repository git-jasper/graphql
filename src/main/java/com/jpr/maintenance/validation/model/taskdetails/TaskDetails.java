package com.jpr.maintenance.validation.model.taskdetails;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.control.Either;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class TaskDetails {

    private static final Pattern descriptionWhitelist = Pattern.compile("[a-zA-Z ]{1,100}");

    private final String description;
    private final Integer interval_km;
    private final Integer interval_months;

    private TaskDetails(String description, Integer interval_km, Integer interval_months) {
        this.description = description;
        this.interval_km = interval_km;
        this.interval_months = interval_months;
    }

    public static Either<InputValidationError, TaskDetails> of(DataFetchingEnvironment environment) {
        final String description = environment.getArgument("description");
        boolean validDescription = descriptionWhitelist.matcher(description).matches();
        if (!validDescription) {
            return Either.left(InputValidationError.INVALID_DESCRIPTION);
        }
        return Either.right(new TaskDetails(
            description,
            environment.getArgument("interval_km"),
            environment.getArgument("interval_months")
        ));
    }

}
