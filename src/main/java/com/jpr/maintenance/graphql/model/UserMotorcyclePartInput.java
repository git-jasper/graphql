package com.jpr.maintenance.graphql.model;

import javax.validation.constraints.NotNull;

public record UserMotorcyclePartInput(@NotNull Long userMotorcycleId,
                                      @NotNull Long partId) {
}
