package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.TaskDetails;
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
        assertTrue(service.findById(0).isPresent());
    }

    @Test
    void findDetailsByIdEmpty() {
        assertTrue(service.findById(-1).isEmpty());
    }

    @Test
    void create() {
        TaskDetails details = new TaskDetails();
        assertEquals(details, service.save(details));
    }
}