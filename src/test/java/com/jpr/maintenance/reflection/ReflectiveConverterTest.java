package com.jpr.maintenance.reflection;


import com.jpr.maintenance.graphql.model.TaskDetailsInput;
import graphql.GraphQLError;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jpr.maintenance.validation.errors.InputValidationError.FAILED_TO_INSTANTIATE_OBJECT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectiveConverterTest {

    @Test
    public void toObjectOk(){
        Map<String, Object> map = Map.of(
            "description", "test",
            "interval_km", 1,
            "interval_months", 2
        );
        TaskDetailsInput taskDetailsInput = ReflectiveConverter.toObject(map, TaskDetailsInput.class).get();

        assertEquals("test", taskDetailsInput.getDescription());
        assertEquals(1, taskDetailsInput.getInterval_km());
        assertEquals(2, taskDetailsInput.getInterval_months());
    }

    @Test
    public void toObjectFailed(){
        Map<String, Object> map = new HashMap<>();
        GraphQLError error = ReflectiveConverter.toObject(map, TestClass.class).getLeft();

        assertEquals(FAILED_TO_INSTANTIATE_OBJECT, error.getErrorType());
    }

    static class TestClass {
        private String missingConstructor;

        public TestClass() {
        }
    }

}