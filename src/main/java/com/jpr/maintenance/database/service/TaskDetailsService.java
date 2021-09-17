package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.repository.TaskDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TaskDetailsService { // TODO better name than ...service ?

    private final TaskDetailsRepository repository;

    public TaskDetailsService(TaskDetailsRepository repository) {
        this.repository = repository;
    }

    public Optional<TaskDetailsEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public TaskDetailsEntity save(TaskDetailsEntity taskDetails) {
        TaskDetailsEntity entity = repository.save(taskDetails);
        log.info(entity.toString());
        return entity;
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
