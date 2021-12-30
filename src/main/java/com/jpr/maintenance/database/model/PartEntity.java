package com.jpr.maintenance.database.model;

import com.jpr.maintenance.graphql.model.PartInput;
import com.jpr.maintenance.validation.ValidationService;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

@Data
@Builder
@Table("part")
public class PartEntity {
    @Id
    private Long id;
    private String brand;
    private String partNr;
    private String description;

    public static Mono<PartEntity> of(PartInput input) {
        return ValidationService.validate(input)
            .map(i -> PartEntity.builder()
                .brand(i.brand())
                .partNr(i.partNr())
                .description(i.description())
                .build()
            );
    }
}
