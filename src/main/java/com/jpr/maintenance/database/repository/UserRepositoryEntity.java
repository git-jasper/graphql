package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepositoryEntity extends ReactiveCrudRepository<UserEntity, Long>, EntityTemplateUserRepository {
    @Query("SELECT * FROM \"user\" WHERE username = :username")
    Mono<UserEntity> findByUserName(String username);

    @Modifying
    @Query("DELETE FROM \"user\" WHERE username = :username AND \"password\" = :password")
    Mono<Integer> removeByUsernameAndPassword(String username, String password);
}
