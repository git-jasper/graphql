package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.repository.MotorcycleRepository;
import com.jpr.maintenance.database.service.MotorcycleService;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphQLDataFetchersTest {
    private MotorcycleService service;
    private GraphQLDataFetchers graphQLDataFetchers;

    @BeforeEach
    void setup() {
        service =  mock(MotorcycleService.class);
        graphQLDataFetchers = new GraphQLDataFetchers(service);
    }

    @Test
    void motorcycleByIdOk() throws Exception {
        when(service.findById(anyLong())).thenReturn(Mono.just(MotorcycleEntity.builder().build()));
        Map<String, Object> arguments = Map.of("id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        var dataFetcher = graphQLDataFetchers.getMotorcycleById().getDataFetcher();
        var motorcycle = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);

        assertNotNull(motorcycle);
    }

//    @Test
//    void taskDetailsByIdNull() throws Exception {
//        Map<String, Object> arguments = Map.of("task_id", "-1");
//        var environment = new DataFetchingEnvironmentImpl.Builder()
//            .arguments(arguments)
//            .build();
//        DataFetcher<TaskDetailsEntity> dataFetcher = graphQLDataFetchers.getTaskDetailsById().getDataFetcher();
//        TaskDetailsEntity taskDetails = dataFetcher.get(environment);
//
//        assertNull(taskDetails);
//    }
//
//    @Test
//    void createTaskDetailsOk() throws Exception {
//        Map<String, Object> map = Map.of(
//            "description", "description",
//            "interval_km", 5000,
//            "interval_months", 48
//        );
//        var environment = new DataFetchingEnvironmentImpl.Builder()
//            .arguments(Map.of("taskDetailsInput", map))
//            .build();
//        DataFetcher<DataFetcherResult<TaskDetailsEntity>> dataFetcher = graphQLDataFetchers.createTaskDetails().getDataFetcher();
//        DataFetcherResult<TaskDetailsEntity> dataFetcherResult = dataFetcher.get(environment);
//        TaskDetailsEntity resultData = dataFetcherResult.getData();
//
//        assertEquals("description", resultData.getDescription());
//        assertEquals(5000, resultData.getInterval_km());
//        assertEquals(48, resultData.getInterval_months());
//        assertTrue(dataFetcherResult.getErrors().isEmpty());
//    }
//
//    @Test
//    void createTaskDetailsUnhappyFlow() throws Exception {
//        Map<String, Object> arguments = Map.of(
//            "taskDetailsInput", new HashMap<>()
//        );
//        var environment = new DataFetchingEnvironmentImpl.Builder()
//            .arguments(arguments)
//            .build();
//        DataFetcher<DataFetcherResult<TaskDetailsEntity>> dataFetcher = graphQLDataFetchers.createTaskDetails().getDataFetcher();
//        DataFetcherResult<TaskDetailsEntity> dataFetcherResult = dataFetcher.get(environment);
//
//        assertNull(dataFetcherResult.getData());
//    }
//
//    @Test
//    void deleteTaskDetails() throws Exception {
//        Map<String, Object> arguments = Map.of("task_id", "0");
//        var environment = new DataFetchingEnvironmentImpl.Builder()
//            .arguments(arguments)
//            .build();
//        DataFetcher<Void> dataFetcher = graphQLDataFetchers.deleteTaskDetails().getDataFetcher();
//        Void unused = dataFetcher.get(environment);
//
//        assertNull(unused);
//    }
}