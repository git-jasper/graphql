package com.jpr.maintenance.db.service;

import com.jpr.maintenance.db.model.TaskDetails;
import com.jpr.maintenance.db.repository.TaskDetailsRepository;
import com.jpr.maintenance.db.testing.TaskDetailsRepositoryTestImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskDetailsServiceTest {

    private final TaskDetailsRepository repository = new TaskDetailsRepositoryTestImpl();
    private final TaskDetailsService service = new TaskDetailsService(repository);

    @Test
    void findDetails() {
        assertTrue(service.findById(0).isEmpty());
    }

    @Test
    void create() {
        TaskDetails details = new TaskDetails();
        assertEquals(details, service.save(details));
    }
}