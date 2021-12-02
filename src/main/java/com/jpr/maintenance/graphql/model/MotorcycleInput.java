package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.model.Brand;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record MotorcycleInput(Brand brand,
                              @NotEmpty String name,
                              @NotNull @Max(10000) @Min(1) Integer engineSize) {}
