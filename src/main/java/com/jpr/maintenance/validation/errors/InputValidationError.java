package com.jpr.maintenance.validation.errors;

import graphql.ErrorClassification;

import java.util.function.Function;

public enum InputValidationError implements ErrorClassification {

    INVALID_FIELD(fun("Field [%s] does not comply to specified format")),
    FAILED_TO_INSTANTIATE_PASSWORD(fun("Failed to instantiate password object")),
    // TODO below are not input validation error
    DATA_ACCESS_ERROR(fun("Error occurred while trying to access the database")),
    USER_ACCESS_ERROR(fun("Failed to access user")),
    UNEXPECTED_ERROR(fun("Unexpected error occurred, caused by [%s]")),
    AUTHENTICATION_FAILED(fun("Failed to authenticate"));

    private final Function<String, String> errorMessageFun;

    InputValidationError(Function<String, String> errorMessageFun) {
        this.errorMessageFun = errorMessageFun;
    }

    public String getErrorMessage(String arg) {
        return errorMessageFun.apply(arg);
    }

    private static Function<String, String> fun(String templateText) {
        return arg -> String.format(templateText, arg);
    }
}
