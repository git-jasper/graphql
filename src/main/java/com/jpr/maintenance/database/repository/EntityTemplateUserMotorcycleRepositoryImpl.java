package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EntityTemplateUserMotorcycleRepositoryImpl implements EntityTemplateUserMotorcycleRepository {
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserMotorcycleEntity> saveUserMotorcycle(Long userId, Long motorcycleId, String color) {
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
                    u.id AS "user.id",\s
                    u.username AS "user.username",\s
                    u.password AS "user.password",\s
                    m.id AS "motorcycle.id",\s
                    m.brand AS "motorcycle.brand",\s
                    m.name AS "motorcycle.name",\s
                    m.engine_size  AS "motorcycle.engine_size",
                    ins.color AS "color",
                    ins.id AS "id"
                FROM
                    "user" u
                LEFT JOIN
                    ins
                ON
                    u.id = ins.user_id
                LEFT JOIN
                    motorcycle m
                ON
                    m.id = ins.motorcycle_id
                WHERE
                    u.id=$1
                """)
            .bind("$1", userId)
            .bind("$2", motorcycleId)
            .bind("$3", color)
            .fetch()
            .first()
            .map(m -> UserMotorcycleEntity.builder()
                .id((Long) m.get("id"))
                .color((String) m.get("color"))
                .user(UserEntity.builder()
                    .id((Long) m.get("user.id"))
                    .username((String) m.get("user.username"))
                    .password((String) m.get("user.password"))
                    .build()
                )
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
