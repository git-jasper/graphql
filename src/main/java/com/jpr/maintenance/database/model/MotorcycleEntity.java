package com.jpr.maintenance.database.model;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.validation.ValidationService;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

@Data
@Builder
@Table("motorcycle")
public class MotorcycleEntity {
    @Id
    private Long id;
    private Brand brand;
    private String name;
    private Integer engineSize;

    public static Mono<MotorcycleEntity> of(MotorcycleInput input) {
        return ValidationService.validate(input)
            .map(i -> MotorcycleEntity
                .builder()
                .brand(input.brand())
                .name(input.name())
                .engineSize(input.engineSize())
                .build()
            );
    }
}
