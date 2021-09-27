package com.jpr.maintenance.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskDetailsInput {

    private final String description;
    private final Integer interval_km;
    private final Integer interval_months;
}
