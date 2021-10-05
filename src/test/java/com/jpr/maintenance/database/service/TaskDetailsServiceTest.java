package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.repository.TaskDetailsRepository;
import com.jpr.maintenance.database.testing.TaskDetailsRepositoryTestImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskDetailsServiceTest {

    private final TaskDetailsRepository repository = new TaskDetailsRepositoryTestImpl();
    private final TaskDetailsService service = new TaskDetailsService(repository);

    @Test
    void findDetailsByIdOk() {
        assertTrue(service.findById(0L).isPresent());
    }

    @Test
    void findDetailsByIdEmpty() {
        assertTrue(service.findById(-1L).isEmpty());
    }

    @Test
    void create() {
        TaskDetailsEntity details = new TaskDetailsEntity();
        assertEquals(details, service.save(details));
    }
}