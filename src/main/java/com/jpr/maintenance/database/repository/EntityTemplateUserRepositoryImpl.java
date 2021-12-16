package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

import static com.jpr.maintenance.reflection.ObjectMapper.toObject;

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
            .mapNotNull(result -> toObject(result, UserEntity.class).getOrNull());
    }
}
