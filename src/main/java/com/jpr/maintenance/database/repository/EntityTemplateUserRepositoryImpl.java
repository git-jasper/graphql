package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.model.Brand;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static com.jpr.maintenance.util.ReflectionUtil.deserializeToPojo;

@RequiredArgsConstructor
public class EntityTemplateUserRepositoryImpl implements EntityTemplateUserRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserEntity> saveUser(UserEntity userEntity) {
        return template.getDatabaseClient()
            .sql("INSERT INTO \"user\" (username, \"password\", salt) VALUES ($1, $2, $3) RETURNING id,username,\"password\",salt")
            .bind("$1", userEntity.getUsername())
            .bind("$2", userEntity.getPassword())
            .bind("$3", userEntity.getSalt())
            .fetch()
            .first()
            .flatMap(result -> deserializeToPojo(result, UserEntity.class));
    }

    @Override
    public Mono<UserEntity> findByUserName(String username) {
        return template.getDatabaseClient()
            .sql(
                """
                    SELECT
                         u.id,
                         u.username,
                         u.password,
                         u.salt,
                         m.id          AS "motorcycle.id",
                         m.brand       AS "motorcycle.brand",
                         m.name        AS "motorcycle.name",
                         m.engine_size AS "motorcycle.engine_size",
                         um.id         AS "user_motorcycle.id",
                         um.color      AS "user_motorcycle.color",
                         ump.id        AS "user_motorcycle_part.id",
                         p.id          AS "part.id",
                         p.brand       AS "part.brand",
                         p.part_nr     AS "part.part_nr",
                         p.description AS "part.description"
                     FROM
                         "user" u
                     LEFT JOIN
                         user_motorcycle um
                     ON
                         u.id = um.user_id
                     LEFT JOIN
                         motorcycle m
                     ON
                         m.id = um.motorcycle_id
                     LEFT JOIN
                         user_motorcycle_part ump
                     ON
                         um.id=ump.user_motorcycle_id
                     LEFT JOIN
                         part p
                     ON
                         ump.part_id=p.id
                     WHERE
                         u.username=$1
                    """
            )
            .bind("$1", username)
            .fetch()
            .all()
            .bufferUntilChanged(result -> result.get("id"))
            .map(this::constructUserEntity)
            .next();
    }

    private UserEntity constructUserEntity(java.util.List<Map<String, Object>> data) {
        List<Map<String, Object>> vavrList = List.ofAll(data);
        Map<String, Object> head = vavrList.head();
        return UserEntity.builder()
            .id((Long) head.get("id"))
            .username((String) head.get("username"))
            .password((String) head.get("password"))
            .salt((String) head.get("salt"))
            .motorcycles(
                constructMotorcycles(data
                    .stream()
                    .filter(e -> e.get("user_motorcycle.id") != null)
                    .collect(Collectors.groupingBy(m -> m.get("user_motorcycle.id")))
                )
            )
            .build();
    }

    private java.util.List<UserMotorcycleEntity> constructMotorcycles(Map<Object, java.util.List<Map<String, Object>>> data) {
        return data
            .values()
            .stream()
            .map(this::constructMotorcycle)
            .collect(Collectors.toList());
    }

    private UserMotorcycleEntity constructMotorcycle(java.util.List<Map<String, Object>> data) {
        List<Map<String, Object>> vavrList = List.ofAll(data);
        Map<String, Object> head = vavrList.head();
        return UserMotorcycleEntity.builder()
            .id((Long) head.get("user_motorcycle.id"))
            .color((String) head.get("user_motorcycle.color"))
            .motorcycle(MotorcycleEntity.builder()
                .id((Long) head.get("motorcycle.id"))
                .brand(Brand.fromString((String) head.get("motorcycle.brand")))
                .name((String) head.get("motorcycle.name"))
                .engineSize((Integer) head.get("motorcycle.engine_size"))
                .build())
            .parts(constructParts(
                vavrList
                    .filter(m -> m.get("user_motorcycle_part.id") != null)
            ))
            .build();
    }

    private java.util.List<PartEntity> constructParts(List<Map<String, Object>> data) {
        return data
            .map(this::constructPart)
            .collect(Collectors.toList());
    }

    private PartEntity constructPart(Map<String, Object> data) {
        return PartEntity.builder()
            .id((Long) data.get("part.id"))
            .brand((String) data.get("part.brand"))
            .partNr((String) data.get("part.part_nr"))
            .description((String) data.get("part.description"))
            .build();
    }
}
