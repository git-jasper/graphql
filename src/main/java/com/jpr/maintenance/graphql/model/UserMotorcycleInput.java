package com.jpr.maintenance.graphql.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record UserMotorcycleInput(@NotNull Long userId,
                                  @NotNull Long motorcycleId,
                                  @NotEmpty String color) {
}
