package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.TaskDetails;
import com.jpr.maintenance.database.service.TaskDetailsService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class GraphQLDataFetchers {

    private final TaskDetailsService service;

    public GraphQLDataFetchers(TaskDetailsService service) {
        this.service = service;
    }

    public DataFetcher<TaskDetails> getTaskDetailsIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String taskId = dataFetchingEnvironment.getArgument("task_id");
            return service.findById(Integer.valueOf(taskId)).stream().findFirst().orElse(null);
        };
    }

    public DataFetcher<TaskDetails> createTaskDetails() {
        return dataFetchingEnvironment -> {
            TaskDetails taskDetails = TaskDetails.of(dataFetchingEnvironment);
            return service.save(taskDetails);
        };
    }

    public DataFetcher<Boolean> deleteTaskDetails() {
        return dataFetchingEnvironment -> {
            String task_id = dataFetchingEnvironment.getArgument("task_id");
            service.deleteById(Integer.valueOf(task_id));
            return null;
        };
    }
}
