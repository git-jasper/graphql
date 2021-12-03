package com.jpr.maintenance.validation.errors;

import graphql.ErrorClassification;

import java.util.function.Function;

public enum InputValidationError implements ErrorClassification {

    FAILED_TO_INSTANTIATE_OBJECT(fun("Failed to instantiate object of type [%s]")),
    FAILED_TO_RESOLVE_FIELD(fun("Failed to resolve field [%s]")),
    INVALID_FIELD(fun("Field [%s] does not comply to specified format")),
    DATA_PERSISTENCE_ERROR(fun("Data persistence error")),
    NULL_VALUE(fun("Field [%s] cannot be null"));

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
