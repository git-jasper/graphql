package com.jpr.maintenance.graphql.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record UserInput(@NotBlank String username,
                        @NotBlank @Length(min = 6, max = 255) String password) {}
