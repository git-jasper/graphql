package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jpr.maintenance.model.Brand.DUCATI;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphQLDataFetchersTest {
    private MotorcycleService service;
    private GraphQLDataFetchers graphQLDataFetchers;

    @BeforeEach
    void setup() {
        service = mock(MotorcycleService.class);
        graphQLDataFetchers = new GraphQLDataFetchers(service);
    }

    @Test
    void motorcycleByIdOk() throws Exception {
        when(service.findById(0L)).thenReturn(Mono.just(MotorcycleEntity.builder().build()));
        Map<String, Object> arguments = Map.of("id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = graphQLDataFetchers.getMotorcycleById().getDataFetcher();
        var motorcycle = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertNotNull(motorcycle);
    }

    @Test
    void mototcycleByIdNull() throws Exception {
        when(service.findById(-1L)).thenReturn(Mono.empty());
        Map<String, Object> arguments = Map.of("id", "-1");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = graphQLDataFetchers.getMotorcycleById().getDataFetcher();
        var motorcycle = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertNull(motorcycle);
    }

    @Test
    void createMotorcycleOk() throws Exception {
        when(service.save(any(MotorcycleEntity.class)))
            .thenReturn(
                Mono.just(
                    MotorcycleEntity
                        .builder()
                        .brand(DUCATI)
                        .name("999R")
                        .engineSize(999)
                        .build()
                )
            );

        Map<String, Object> map = Map.of(
            "brand", "DUCATI",
            "name", "999R",
            "engineSize", 999
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(Map.of("motorcycleInput", map))
            .build();
        var dataFetcher = graphQLDataFetchers.createMotorcycle().getDataFetcher();
        var dataFetcherResult = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);
        var resultData = dataFetcherResult.getData();

        assertEquals(DUCATI, resultData.getBrand());
        assertEquals("999R", resultData.getName());
        assertEquals(999, resultData.getEngineSize());
        assertTrue(dataFetcherResult.getErrors().isEmpty());
    }

    @Test
    void createMotorcycleUnhappyFlow() throws Exception {
        when(service.save(any(MotorcycleEntity.class)))
            .thenReturn(
                Mono.just(
                    MotorcycleEntity
                        .builder()
                        .brand(DUCATI)
                        .name("999R")
                        .engineSize(999)
                        .build()
                )
            );

        Map<String, Object> arguments = Map.of(
            "motorcycleInput", new HashMap<>()
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = graphQLDataFetchers.createMotorcycle().getDataFetcher();
        var dataFetcherResult = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertNull(dataFetcherResult.getData());
        assertEquals("Field [brand] cannot be null", dataFetcherResult.getErrors().get(0).getMessage());
    }

    @Test
    void deleteTMotorcycleOk() throws Exception {
        when(service.deleteById(0L)).thenReturn(Mono.just(true));
        Map<String, Object> arguments = Map.of("id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = graphQLDataFetchers.deleteMotorcycle().getDataFetcher();
        var dataFetcherResult = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertTrue(dataFetcherResult);
    }
}