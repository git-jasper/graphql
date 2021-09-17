package com.jpr.maintenance.validation.errors;

import graphql.ErrorClassification;

public enum InputValidationError implements ErrorClassification {

    INVALID_DESCRIPTION, DATA_PERSISTENCE_ERROR;
}
