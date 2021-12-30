package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.tailrecursion.TailCall;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.jpr.maintenance.tailrecursion.TailCalls.done;
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
                        m.id AS "motorcycle.id",
                        m.brand AS "motorcycle.brand",
                        m.name AS "motorcycle.name",
                        m.engine_size  AS "motorcycle.engine_size",
                        um.id AS "user_motorcycle.id",
                        um.color AS "user_motorcycle.color"
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
                constructMotorcyclesRecursively(
                    List.empty(),
                    vavrList.filter(e -> e.get("user_motorcycle.id") != null)).invoke().asJava())
            .build();
    }

    private TailCall<List<UserMotorcycleEntity>> constructMotorcyclesRecursively(List<UserMotorcycleEntity> accumulator,
                                                                                 List<Map<String, Object>> data) {
        if (data.isEmpty()) {
            return done(accumulator);
        } else {
            Map<String, Object> entry = data.head();
            return () -> constructMotorcyclesRecursively(accumulator.append(
                UserMotorcycleEntity.builder()
                    .id((Long) entry.get("user_motorcycle.id"))
                    .color((String) entry.get("user_motorcycle.color"))
                    .motorcycle(MotorcycleEntity.builder()
                        .id((Long) entry.get("motorcycle.id"))
                        .brand(entry.get("motorcycle.brand") != null ? Brand.valueOf((String) entry.get("motorcycle.brand")) : null)
                        .name((String) entry.get("motorcycle.name"))
                        .engineSize((Integer) entry.get("motorcycle.engine_size"))
                        .build())
                    .build()),
                data.tail()
            );
        }
    }
}
