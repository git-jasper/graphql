package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import reactor.core.publisher.Mono;

public interface EntityTemplateUserRepository {

    Mono<UserEntity> saveUser(UserEntity userEntity);

    Mono<UserEntity> findByUserName(String username);
}
