package com.jpr.maintenance.validation.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class InputValidationException extends RuntimeException {
    private final InputValidationError error;
    private final String message;
    private final List<Object> path;
}
