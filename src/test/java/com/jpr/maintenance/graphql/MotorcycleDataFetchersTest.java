package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import com.jpr.maintenance.graphql.datafetcher.MotorcycleDataFetchers;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jpr.maintenance.model.Brand.DUCATI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MotorcycleDataFetchersTest {
    private MotorcycleService service;
    private MotorcycleDataFetchers motorcycleDataFetchers;

    @BeforeEach
    void setup() {
        service = mock(MotorcycleService.class);
        motorcycleDataFetchers = new MotorcycleDataFetchers(service);
    }

    @Test
    void motorcycleByIdOk() throws Exception {
        when(service.findById(0L)).thenReturn(Mono.just(MotorcycleEntity.builder().build()));
        Map<String, Object> arguments = Map.of("id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = motorcycleDataFetchers.getMotorcycleById().getDataFetcher();
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
        var dataFetcher = motorcycleDataFetchers.getMotorcycleById().getDataFetcher();
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
        var dataFetcher = motorcycleDataFetchers.createMotorcycle().getDataFetcher();
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
        var dataFetcher = motorcycleDataFetchers.createMotorcycle().getDataFetcher();
        var dataFetcherResult = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertNull(dataFetcherResult.getData());
        assertTrue(dataFetcherResult.getErrors().get(0).getMessage().contains("Field engineSize, constraint: must not be null, actual value: null."));
        assertTrue(dataFetcherResult.getErrors().get(0).getMessage().contains("Field brand, constraint: must not be null, actual value: null."));
        assertTrue(dataFetcherResult.getErrors().get(0).getMessage().contains("Field name, constraint: must not be empty, actual value: null."));
    }

    @Test
    void deleteTMotorcycleOk() throws Exception {
        when(service.deleteById(0L)).thenReturn(Mono.just(true));
        Map<String, Object> arguments = Map.of("id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = motorcycleDataFetchers.deleteMotorcycle().getDataFetcher();
        var dataFetcherResult = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertTrue(dataFetcherResult.getData());
    }
}