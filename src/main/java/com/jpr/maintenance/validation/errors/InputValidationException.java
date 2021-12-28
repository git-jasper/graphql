package com.jpr.maintenance.validation.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InputValidationException extends RuntimeException {
    private final InputValidationError error;
    private final String message;
}
