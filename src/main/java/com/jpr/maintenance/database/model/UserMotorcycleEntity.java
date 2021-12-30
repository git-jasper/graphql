package com.jpr.maintenance.database.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("user_motorcycle")
public class UserMotorcycleEntity {
    @Id
    private Long id;
    private MotorcycleEntity motorcycle;
    private String color;
}
