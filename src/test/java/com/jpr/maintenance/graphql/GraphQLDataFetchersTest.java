package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.TaskDetails;
import com.jpr.maintenance.database.repository.TaskDetailsRepository;
import com.jpr.maintenance.database.service.TaskDetailsService;
import com.jpr.maintenance.database.testing.TaskDetailsRepositoryTestImpl;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        DataFetcher<TaskDetails> dataFetcher = graphQLDataFetchers.getTaskDetailsById();
        TaskDetails taskDetails = dataFetcher.get(environment);

        assertNotNull(taskDetails);
    }

    @Test
    void taskDetailsByIdNull() throws Exception {
        Map<String, Object> arguments = Map.of("task_id", "-1");
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();
        DataFetcher<TaskDetails> dataFetcher = graphQLDataFetchers.getTaskDetailsById();
        TaskDetails taskDetails = dataFetcher.get(environment);

        assertNull(taskDetails);
    }

    @Test
    void createTaskDetailsOk() throws Exception {
        var environment = new DataFetchingEnvironmentImpl.Builder().build();
        DataFetcher<TaskDetails> dataFetcher = graphQLDataFetchers.createTaskDetails();
        TaskDetails taskDetails = dataFetcher.get(environment);

        assertNotNull(taskDetails);
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