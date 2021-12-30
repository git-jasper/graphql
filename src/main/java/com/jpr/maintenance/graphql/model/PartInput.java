package com.jpr.maintenance.graphql.model;

import javax.validation.constraints.NotEmpty;

public record PartInput(@NotEmpty String brand,
                        @NotEmpty String partNr,
                        @NotEmpty String description) {
}
