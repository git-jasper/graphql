package com.jpr.graphql;

import com.jpr.graphql.maintenance.db.model.TaskDetails;
import com.jpr.graphql.maintenance.db.service.TaskDetailsService;
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
            String taskId = dataFetchingEnvironment.getArgument("id");
            return service.findDetails(Integer.valueOf(taskId)).stream().findFirst().orElse(null);
        };
    }
}
