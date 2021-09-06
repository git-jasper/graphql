package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.repository.TaskDetailsRepository;
import com.jpr.maintenance.database.service.TaskDetailsService;
import com.jpr.maintenance.database.testing.TaskDetailsRepositoryTestImpl;
import com.jpr.maintenance.validation.model.taskdetails.TaskDetails;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphQLDataFetchersTest {

    private final TaskDetailsRepository repository = new TaskDetailsRepositoryTestImpl();
    private final TaskDetailsService service = new TaskDetailsService(repository);
    private final GraphQLDataFetchers graphQLDataFetchers = new GraphQLDataFetchers(service);

    @Test
    void taskDetailsByIdOk() throws Exception {
        Map<String, Object> arguments = Map.of("task_id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        DataFetcher<TaskDetailsEntity> dataFetcher = graphQLDataFetchers.getTaskDetailsById();
        TaskDetailsEntity taskDetails = dataFetcher.get(environment);

        assertNotNull(taskDetails);
    }

    @Test
    void taskDetailsByIdNull() throws Exception {
        Map<String, Object> arguments = Map.of("task_id", "-1");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        DataFetcher<TaskDetailsEntity> dataFetcher = graphQLDataFetchers.getTaskDetailsById();
        TaskDetailsEntity taskDetails = dataFetcher.get(environment);

        assertNull(taskDetails);
    }

    @Test
    void createTaskDetailsOk() throws Exception {
        Map<String, Object> arguments = Map.of(
            "description", "description",
            "interval_km", 5000,
            "interval_months", 48
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        DataFetcher<DataFetcherResult<TaskDetails>> dataFetcher = graphQLDataFetchers.createTaskDetails();
        DataFetcherResult<TaskDetails> dataFetcherResult = dataFetcher.get(environment);
        TaskDetails resultData = dataFetcherResult.getData();

        assertEquals("description", resultData.getDescription());
        assertEquals(5000, resultData.getInterval_km());
        assertEquals(48, resultData.getInterval_months());
        assertTrue(dataFetcherResult.getErrors().isEmpty());
    }

    @Test
    void createTaskDetailsUnhappyFlow() throws Exception {
        Map<String, Object> arguments = Map.of(
            "description", "132"
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        DataFetcher<DataFetcherResult<TaskDetails>> dataFetcher = graphQLDataFetchers.createTaskDetails();
        DataFetcherResult<TaskDetails> dataFetcherResult = dataFetcher.get(environment);

        assertNull(dataFetcherResult.getData());
    }

    @Test
    void deleteTaskDetails() throws Exception {
        Map<String, Object> arguments = Map.of("task_id", "0");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        DataFetcher<Void> dataFetcher = graphQLDataFetchers.deleteTaskDetails();
        Void unused = dataFetcher.get(environment);

        assertNull(unused);
    }
}