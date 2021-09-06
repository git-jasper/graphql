package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.service.TaskDetailsService;
import com.jpr.maintenance.validation.errors.InputValidationError;
import com.jpr.maintenance.validation.model.taskdetails.TaskDetails;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class GraphQLDataFetchers {

    private final TaskDetailsService service;

    public GraphQLDataFetchers(TaskDetailsService service) {
        this.service = service;
    }

    //TODO refactor to return DataFetcherResult
    public DataFetcher<TaskDetailsEntity> getTaskDetailsById() {
        return dataFetchingEnvironment -> {
            String taskId = dataFetchingEnvironment.getArgument("task_id");
            return service.findById(Integer.valueOf(taskId)).stream().findFirst().orElse(null);
        };
    }

    public DataFetcher<DataFetcherResult<TaskDetails>> createTaskDetails() {
        return dataFetchingEnvironment -> {
            DataFetcherResult.Builder<TaskDetails> resultBuilder = DataFetcherResult.newResult();
            TaskDetails.of(dataFetchingEnvironment)
                .peekLeft(handleErrors())
                .peek(details -> {
                    service.save(TaskDetailsEntity.of(details));
                    resultBuilder.data(details);
                });
            return resultBuilder.build();
        };
    }

    // TODO move error handling to its own class
    private Consumer<InputValidationError> handleErrors() {
        return error -> {
            System.out.println(error.name());
        };
    }

    //TODO refactor to return DataFetcherResult
    public DataFetcher<Void> deleteTaskDetails() {
        return dataFetchingEnvironment -> {
            String task_id = dataFetchingEnvironment.getArgument("task_id");
            service.deleteById(Integer.valueOf(task_id));
            return null;
        };
    }
}
