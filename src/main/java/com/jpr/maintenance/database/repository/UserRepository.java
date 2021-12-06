package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    @Query("SELECT * FROM \"user\" WHERE username = :username AND \"password\" = :password")
    Mono<UserEntity> findByUserNameAndPassword(String username, String password);

    @Modifying
    @Query("INSERT INTO \"user\" (username, \"password\") VALUES (&username, &password)")
    Mono<Integer> saveUser(String username, String password);

    @Modifying
    @Query("DELETE FROM \"user\" WHERE username = :username AND \"password\" = :password")
    Mono<Integer> removeByUsernameAndPassword(String username, String password);
}
