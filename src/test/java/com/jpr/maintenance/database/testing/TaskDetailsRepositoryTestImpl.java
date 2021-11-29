package com.jpr.maintenance.database.testing;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.repository.TaskDetailsRepository;

import java.util.Optional;

import static java.util.Collections.singleton;

public class TaskDetailsRepositoryTestImpl implements TaskDetailsRepository {

    @Override
    public <S extends TaskDetailsEntity> S save(S s) {
        return s;
    }

    @Override
    public <S extends TaskDetailsEntity> Iterable<S> saveAll(Iterable<S> iterable) {
        return iterable;
    }

    @Override
    public Optional<TaskDetailsEntity> findById(Long id) {
        if (id >= 0) {
            return Optional.of(new TaskDetailsEntity());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public Iterable<TaskDetailsEntity> findAll() {
        return singleton(new TaskDetailsEntity());
    }

    @Override
    public Iterable<TaskDetailsEntity> findAllById(Iterable<Long> iterable) {
        return singleton(new TaskDetailsEntity());
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(TaskDetailsEntity taskDetails) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends TaskDetailsEntity> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
