package com.jpr.maintenance.testutil;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.model.Password;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings(value = "NullableProblems")
public class DummyUserRepository implements UserRepository {
    @Override
    public Mono<UserEntity> saveUser(UserEntity userEntity) {
        return Mono.just(userEntity);
    }

    @Override
    public Mono<UserEntity> findByUserName(String username) {
        return Mono.just(UserEntity.builder()
            .id(1L)
            .username(username)
            .password(Password.of("secret", "salt").get().password())
            .salt("salt")
            .build());
    }

    @Override
    public Mono<Integer> removeById(Long id) {
        return Mono.just(id.compareTo(0L));
    }

    @Override
    public <S extends UserEntity> Mono<S> save(S entity) {
        return null;
    }

    @Override
    public <S extends UserEntity> Flux<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends UserEntity> Flux<S> saveAll(Publisher<S> entityStream) {
        return null;
    }

    @Override
    public Mono<UserEntity> findById(Long aLong) {
        return null;
    }

    @Override
    public Mono<UserEntity> findById(Publisher<Long> id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Long aLong) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<Long> id) {
        return null;
    }

    @Override
    public Flux<UserEntity> findAll() {
        return null;
    }

    @Override
    public Flux<UserEntity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public Flux<UserEntity> findAllById(Publisher<Long> idStream) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Long aLong) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Publisher<Long> id) {
        return null;
    }

    @Override
    public Mono<Void> delete(UserEntity entity) {
        return null;
    }

    @Override
    public Mono<Void> deleteAllById(Iterable<? extends Long> longs) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends UserEntity> entities) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends UserEntity> entityStream) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
