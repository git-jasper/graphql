package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.model.Brand;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record MotorcycleInput(@NotNull Brand brand,
                              @NotEmpty @Length(min=1, max=255) String name,
                              @NotNull @Max(10000) @Min(1) Integer engineSize) {}
