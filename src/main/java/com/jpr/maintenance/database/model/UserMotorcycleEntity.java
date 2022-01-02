package com.jpr.maintenance.database.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode
@Setter
@Getter
@Builder
@Table("user_motorcycle")
public class UserMotorcycleEntity {
    @Id
    private Long id;
    private MotorcycleEntity motorcycle;
    private String color;
}
