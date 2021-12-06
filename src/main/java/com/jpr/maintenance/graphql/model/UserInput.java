package com.jpr.maintenance.graphql.model;

import javax.validation.constraints.NotBlank;

public record UserInput(@NotBlank String username,
                        @NotBlank String password) {}
