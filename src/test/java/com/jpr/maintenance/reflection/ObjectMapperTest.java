package com.jpr.maintenance.reflection;

import com.jpr.maintenance.graphql.model.MotorcycleInput;
import graphql.GraphQLError;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jpr.maintenance.model.Brand.DUCATI;
import static com.jpr.maintenance.validation.errors.InputValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectMapperTest {

    @Test
    public void toObjectOk(){
        Map<String, Object> map = Map.of(
            "brand", "DUCATI",
            "name", "999R",
            "engineSize", 999
        );
        MotorcycleInput input = ObjectMapper.toObject(map, MotorcycleInput.class).get();

        assertEquals(DUCATI, input.brand());
        assertEquals("999R", input.name());
        assertEquals(999, input.engineSize());
    }

    @Test
    public void toObjectFailedToResolveField(){
        Map<String, Object> map = Map.of("name", "test");
        GraphQLError error = ObjectMapper.toObject(map, MissingConstructor.class).getLeft();

        assertEquals(FAILED_TO_INSTANTIATE_OBJECT, error.getErrorType());
    }

    @Test
    public void toObjectNullValue(){
        Map<String, Object> map = new HashMap<>();
        GraphQLError error = ObjectMapper.toObject(map, MotorcycleInput.class).getLeft();

        assertEquals(NULL_VALUE, error.getErrorType());
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