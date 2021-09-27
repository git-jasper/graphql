package com.jpr.maintenance.validation.errors;

import graphql.ErrorClassification;

public enum InputValidationError implements ErrorClassification {

    FAILED_TO_INSTANTIATE_OBJECT, INVALID_DESCRIPTION, DATA_PERSISTENCE_ERROR;
}
