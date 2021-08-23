package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import com.jpr.maintenance.database.service.TaskDetailsService;
import com.jpr.maintenance.validation.model.taskdetails.TaskDetails;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class GraphQLDataFetchers {

    private final TaskDetailsService service;

    public GraphQLDataFetchers(TaskDetailsService service) {
        this.service = service;
    }

    public DataFetcher<TaskDetailsEntity> getTaskDetailsById() {
        return dataFetchingEnvironment -> {
            String taskId = dataFetchingEnvironment.getArgument("task_id");
            return service.findById(Integer.valueOf(taskId)).stream().findFirst().orElse(null);
        };
    }

    public DataFetcher<TaskDetailsEntity> createTaskDetails() {
        return dataFetchingEnvironment -> {
            var either = TaskDetails.of(dataFetchingEnvironment);
            TaskDetailsEntity entity = either.map(TaskDetailsEntity::of).get();
            return service.save(entity);
        };
    }

    public DataFetcher<Void> deleteTaskDetails() { //TODO is there a better way than return type Void?
        return dataFetchingEnvironment -> {
            String task_id = dataFetchingEnvironment.getArgument("task_id");
            service.deleteById(Integer.valueOf(task_id));
            return null;
        };
    }
}
