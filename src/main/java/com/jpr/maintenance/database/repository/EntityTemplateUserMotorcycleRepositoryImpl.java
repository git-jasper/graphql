package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
import com.jpr.maintenance.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EntityTemplateUserMotorcycleRepositoryImpl implements EntityTemplateUserMotorcycleRepository {
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserMotorcycleEntity> saveUserMotorcycle(UserMotorcycleInput input) {
        return template.getDatabaseClient()
            .sql("""
                WITH ins AS
                ( INSERT INTO
                        user_motorcycle
                        (
                            user_id,
                            motorcycle_id,
                            color
                        )
                        VALUES
                        (
                            $1,$2,$3
                        )
                        RETURNING id,
                        user_id,
                        motorcycle_id,
                        color )
                SELECT
                    m.id AS "motorcycle.id",
                    m.brand AS "motorcycle.brand",
                    m.name AS "motorcycle.name",
                    m.engine_size  AS "motorcycle.engine_size",
                    ins.color AS "color",
                    ins.id AS "id"
                FROM
                    motorcycle m
                LEFT JOIN
                    ins
                ON
                    m.id = ins.motorcycle_id
                WHERE
                    ins.user_id=$1
                AND
                    ins.motorcycle_id=$2
                """)
            .bind("$1", input.userId())
            .bind("$2", input.motorcycleId())
            .bind("$3", input.color())
            .fetch()
            .first()
            .map(m -> UserMotorcycleEntity.builder()
                .id((Long) m.get("id"))
                .color((String) m.get("color"))
                .motorcycle(
                    MotorcycleEntity.builder()
                        .id((Long) m.get("motorcycle.id"))
                        .brand(Brand.valueOf((String) m.get("motorcycle.brand")))
                        .name((String) m.get("motorcycle.name"))
                        .engineSize((Integer) m.get("motorcycle.engine_size"))
                        .build()
                )
                .build()
            );
    }
}
