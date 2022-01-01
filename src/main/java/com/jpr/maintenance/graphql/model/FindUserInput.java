package com.jpr.maintenance.graphql.model;

import javax.validation.constraints.NotEmpty;

public record FindUserInput(@NotEmpty String username) {
}
