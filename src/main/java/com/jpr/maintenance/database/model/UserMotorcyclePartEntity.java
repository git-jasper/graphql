package com.jpr.maintenance.database.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("user_motorcycle_part")
public class UserMotorcyclePartEntity {
    @Id
    private Long id;
    private PartEntity part;
}
