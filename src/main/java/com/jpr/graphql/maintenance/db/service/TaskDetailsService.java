package com.jpr.graphql.maintenance.db.service;

import com.jpr.graphql.maintenance.db.model.TaskDetails;
import com.jpr.graphql.maintenance.db.repository.TaskDetailsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskDetailsService {

    private final TaskDetailsRepository repository;

    public TaskDetailsService(TaskDetailsRepository repository) {
        this.repository = repository;
    }

    public Optional<TaskDetails> findDetails(Integer id) {
        return repository.findById(id);
    }

    public TaskDetails create(TaskDetails taskDetails) {
        return repository.save(taskDetails);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
