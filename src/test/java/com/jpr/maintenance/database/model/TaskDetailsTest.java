package com.jpr.maintenance.database.model;

import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskDetailsTest {

    @Test
    void factoryMethod() {
        Map<String, Object> arguments = Map.of(
            "description", "some description",
            "interval_km", 5000,
            "interval_months", 48
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        TaskDetails details = TaskDetails.of(environment);

        assertNull(details.getTask_id());
        assertEquals("some description", details.getDescription());
        assertEquals(5000, details.getInterval_km());
        assertEquals(48, details.getInterval_months());
    }
}