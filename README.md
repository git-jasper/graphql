##GraphQL application using Spring boot and PostgresQL

- Start PostgresQL docker container, see [README](postgresql/README.md)
- If needed alter [application.properties](src/main/resources/application.properties)
- Start application from IDE or using `mvn spring-boot:run`
- Use tool like GraphQL playground to query application on `http://localhost:8080/graphql`

- For more information on graphql-java see [documentation](https://www.graphql-java.com/documentation/) 

##Example queries

```
query {
    taskDetailsById(task_id: 0) {
        task_id,
        description,
        interval_km,
        interval_months
    }
}

mutation {
    createTaskDetails(description: "a default task", interval_km: 1000, interval_months: 12) {
    task_id
    }
}

mutation {
    deleteTaskDetails(task_id: 2)
}
```