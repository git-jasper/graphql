package com.jpr.maintenance.graphql.datafetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import graphql.execution.DataFetcherResult;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.jpr.maintenance.graphql.handler.ThrowableHandlerProvider.handlerFunction;

public class DataFetcherUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected static <T> Mono<T> deserializeToPojo(Map<String, Object> map, Class<T> clazz) {
        try {
            return Mono.just(OBJECT_MAPPER.convertValue(map, clazz));
        } catch (Exception ex) {
            return Mono.error(ex);
        }
    }
}
