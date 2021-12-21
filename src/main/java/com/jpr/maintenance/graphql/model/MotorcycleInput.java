package com.jpr.maintenance.graphql.model;

import com.jpr.maintenance.model.Brand;
import org.hibernate.validator.constraints.Length;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public record MotorcycleInput(@NotNull Brand brand,
                              @NotEmpty @Length(min=1, max=255) String name,
                              @NotNull @Max(10000) @Min(1) Integer engineSize) {
    public static Mono<MotorcycleInput> of(Map<String, Object> parameters) {
        try {
            Brand brand = Brand.valueOf((String) parameters.get("brand"));
            String name = (String) parameters.get("name");
            Integer engineSize = (Integer) parameters.get("engineSize");
            return Mono.just(new MotorcycleInput(brand, name, engineSize));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
