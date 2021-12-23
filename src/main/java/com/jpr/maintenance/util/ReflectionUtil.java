package com.jpr.maintenance.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ReflectionUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> Mono<T> deserializeToPojo(Map<String, Object> map, Class<T> clazz) {
        try {
            return Mono.just(OBJECT_MAPPER.convertValue(map, clazz));
        } catch (Exception ex) {
            return Mono.error(ex);
        }
    }
}
