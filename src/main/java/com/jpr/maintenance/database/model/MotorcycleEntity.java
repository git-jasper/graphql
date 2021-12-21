package com.jpr.maintenance.database.model;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.validation.ValidationService;
import graphql.GraphQLError;
import io.vavr.control.Either;
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

    public static Either<GraphQLError, MotorcycleEntity> of(MotorcycleInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> Either.right(
                MotorcycleEntity
                    .builder()
                    .brand(input.brand())
                    .name(input.name())
                    .engineSize(input.engineSize())
                    .build()
            ));
    }

    public static Mono<MotorcycleEntity> ofReactive(MotorcycleInput input) {
        return ValidationService.validateReactor(input)
            .map(i -> MotorcycleEntity
                .builder()
                .brand(input.brand())
                .name(input.name())
                .engineSize(input.engineSize())
                .build()
            );
    }
}
