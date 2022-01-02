package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.model.UserMotorcyclePartEntity;
import com.jpr.maintenance.graphql.model.UserMotorcyclePartInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EntityTemplateUserMotorcyclePartRepositoryImpl implements EntityTemplateUserMotorcyclePartRepository {
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserMotorcyclePartEntity> saveUserMotorcyclePart(UserMotorcyclePartInput input) {
        return template.getDatabaseClient()
            .sql("""
                WITH ins AS
               ( INSERT INTO
                       user_motorcycle_part
                       (
                           user_motorcycle_id,
                           part_id
                       )
                       VALUES
                       (
                           $1,
                           $2
                       )
                       RETURNING id,
                       user_motorcycle_id,
                       part_id )
               SELECT
                   p.id          AS "part.id",
                   p.brand       AS "part.brand",
                   p.part_nr     AS "part.part_nr",
                   p.description AS "part.description",
                   ins.id        AS "id"
               FROM
                   part p
               LEFT JOIN
                   ins
               ON
                   p.id = ins.part_id
               WHERE
                   ins.user_motorcycle_id=$1
               AND
                   ins.part_id=$2
                """)
            .bind("$1", input.userMotorcycleId())
            .bind("$2", input.partId())
            .fetch()
            .first()
            .map(m -> UserMotorcyclePartEntity.builder()
                .id((Long) m.get("id"))
                .part(
                    PartEntity.builder()
                        .id((Long) m.get("part.id"))
                        .brand((String) m.get("part.brand"))
                        .partNr((String) m.get("part.part_nr"))
                        .description((String) m.get("part.description"))
                        .build()
                )
                .build()
            );
    }
}
