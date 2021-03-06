package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long>, EntityTemplateUserRepository {
    @Modifying
    @Query("DELETE FROM \"user\" WHERE id = :id")
    Mono<Integer> removeById(Long id);

    @Modifying
    @Query("DELETE FROM \"user\"")
    Mono<Void> deleteAll();
}
