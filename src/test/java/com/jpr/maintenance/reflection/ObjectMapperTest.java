package com.jpr.maintenance.reflection;


import com.jpr.maintenance.graphql.model.TaskDetailsInput;
import graphql.GraphQLError;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jpr.maintenance.validation.errors.InputValidationError.FAILED_TO_INSTANTIATE_OBJECT;
import static com.jpr.maintenance.validation.errors.InputValidationError.FAILED_TO_RESOLVE_FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectMapperTest {

    @Test
    public void toObjectOk(){
        Map<String, Object> map = Map.of(
            "interval_km", 1,
            "description", "test",
            "interval_months", 2
        );
        TaskDetailsInput taskDetailsInput = ObjectMapper.toObject(map, TaskDetailsInput.class).get();

        assertEquals("test", taskDetailsInput.getDescription());
        assertEquals(1, taskDetailsInput.getInterval_km());
        assertEquals(2, taskDetailsInput.getInterval_months());
    }

    @Test
    public void toObjectFailedToResolveField(){
        Map<String, Object> map = Map.of("name", "test");
        GraphQLError error = ObjectMapper.toObject(map, MissingConstructor.class).getLeft();

        assertEquals(FAILED_TO_INSTANTIATE_OBJECT, error.getErrorType());
    }

    @Test
    public void toObjectFailedToInstantiateObject(){
        Map<String, Object> map = new HashMap<>();
        GraphQLError error = ObjectMapper.toObject(map, TaskDetailsInput.class).getLeft();

        assertEquals(FAILED_TO_RESOLVE_FIELD, error.getErrorType());
    }

    @Test
    public void toObjectWithRef(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "main");
        map.put("ref", Map.of("name", "ref", "text", "text"));
        Either<GraphQLError, ClassWithRef> either = ObjectMapper.toObject(map, ClassWithRef.class);

        assertTrue(either.isRight());
        ClassWithRef classWithRef = either.get();

        assertEquals("main", classWithRef.getName());
        assertEquals("ref", classWithRef.getRef().getName());
    }

    @Test
    public void toObjectWithList(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "main");
        map.put("refs", List.of(Map.of("name", "ref1"), Map.of("name", "ref2")));
        Either<GraphQLError, ClassWithListOfRef> either = ObjectMapper.toObject(map, ClassWithListOfRef.class);

        assertTrue(either.isRight());
        ClassWithListOfRef outerClass = either.get();

        assertEquals("main", outerClass.getName());
        assertEquals("ref1", outerClass.getRefs().get(0).getName());
        assertEquals("ref2", outerClass.getRefs().get(1).getName());
    }

    static class MissingConstructor {
        private String name;

        public MissingConstructor() {
        }
    }

    @Getter
    @RequiredArgsConstructor
    static class ClassWithRef {
        private final String name;
        private final RefClass ref;
    }

    @Getter
    @RequiredArgsConstructor
    static class ClassWithListOfRef {
        private final String name;
        private final List<RefClass> refs;
    }

    @Getter
    @RequiredArgsConstructor
    static class RefClass {
        private final String name;
    }

}