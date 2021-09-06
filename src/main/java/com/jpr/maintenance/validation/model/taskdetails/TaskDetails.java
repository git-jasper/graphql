package com.jpr.maintenance.validation.model.taskdetails;

import com.jpr.maintenance.validation.errors.InputValidationError;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.control.Either;
import lombok.Getter;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

@Getter
public class TaskDetails {

    private static final Pattern descriptionWhitelist = Pattern.compile("[a-zA-Z ]{1,100}");
    private static final Pattern integerWhitelist = Pattern.compile("\\d{1,10}");

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
        if (!descriptionWhitelist.matcher(description).matches()) {
            return Either.left(InputValidationError.INVALID_DESCRIPTION);
        }
        String interval_km = environment.getArgument("interval_km"); 
        if (!integerWhitelist.matcher(interval_km).matches()) {
            return Either.left(InputValidationError.INVALID_INTERVAL_KM);
        }
        String interval_months = environment.getArgument("interval_months");
        if (!integerWhitelist.matcher(interval_km).matches()) {
            return Either.left(InputValidationError.INVALID_INTERVAL_MONTHS);
        }
        return Either.right(new TaskDetails(
            description,
            parseInt(interval_km),
            parseInt(interval_months)
        ));
    }

}
