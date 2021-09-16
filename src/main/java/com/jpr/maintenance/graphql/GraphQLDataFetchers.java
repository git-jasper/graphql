package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.service.TaskDetailsService;
import com.jpr.maintenance.validation.model.taskdetails.TaskDetails;
import graphql.GraphQLError;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jpr.maintenance.graphql.GraphQLUtils.*;

@RequiredArgsConstructor
@Configuration
public class GraphQLDataFetchers {
    private final TaskDetailsService service;

    //TODO refactor to return DataFetcherResult
    @Bean
    public DataFetcherWrapper<TaskDetailsEntity> getTaskDetailsById() {
        return new DataFetcherWrapper<>(
                "Query",
                "taskDetailsById",
                dataFetchingEnvironment -> {
                    String taskId = dataFetchingEnvironment.getArgument("task_id");
                    return service.findById(Integer.valueOf(taskId)).stream().findFirst().orElse(null);
                }
        );
    }

    @Bean
    public DataFetcherWrapper<DataFetcherResult<TaskDetailsEntity>> createTaskDetails() {
        return new DataFetcherWrapper<>(
                "Mutation",
                "createTaskDetails",
                dataFetchingEnvironment ->
                        TaskDetails.of(dataFetchingEnvironment)
                                .flatMap(t -> saveTaskDetails(t, dataFetchingEnvironment))
                                .fold(
                                        errorFun(),
                                        successFun()
                                )
        );
    }

    private Either<GraphQLError, TaskDetailsEntity> saveTaskDetails(TaskDetails taskDetails, DataFetchingEnvironment environment) {
        return saveEntity(TaskDetailsEntity.of(taskDetails), service::save, environment);
    }

    //TODO refactor to return DataFetcherResult
    @Bean
    public DataFetcherWrapper<Void> deleteTaskDetails() {
        return new DataFetcherWrapper<>(
                "Mutation",
                "deleteTaskDetails",
                dataFetchingEnvironment -> {
                    String task_id = dataFetchingEnvironment.getArgument("task_id");
                    service.deleteById(Integer.valueOf(task_id));
                    return null;
                }
        );
    }
}
