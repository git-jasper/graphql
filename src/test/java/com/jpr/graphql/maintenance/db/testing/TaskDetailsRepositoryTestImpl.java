package com.jpr.graphql.maintenance.db.testing;

import com.jpr.graphql.maintenance.db.model.TaskDetails;
import com.jpr.graphql.maintenance.db.repository.TaskDetailsRepository;

import java.util.Optional;

import static java.util.Collections.singleton;

public class TaskDetailsRepositoryTestImpl implements TaskDetailsRepository {

    @Override
    public <S extends TaskDetails> S save(S s) {
        return s;
    }

    @Override
    public <S extends TaskDetails> Iterable<S> saveAll(Iterable<S> iterable) {
        return iterable;
    }

    @Override
    public Optional<TaskDetails> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<TaskDetails> findAll() {
        return singleton(new TaskDetails());
    }

    @Override
    public Iterable<TaskDetails> findAllById(Iterable<Integer> iterable) {
        return singleton(new TaskDetails());
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(TaskDetails taskDetails) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends TaskDetails> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
