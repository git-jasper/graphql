package com.jpr.maintenance.database.model;

import graphql.schema.DataFetchingEnvironment;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task_details")
@Getter
public class TaskDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer task_id;
    private String description;
    private Integer interval_km;
    private Integer interval_months;

    public TaskDetails() {}

    private TaskDetails(String description, Integer interval_km, Integer interval_months) {
        this.description = description;
        this.interval_km = interval_km;
        this.interval_months = interval_months;
    }

    public static TaskDetails of(DataFetchingEnvironment environment) {
        return new TaskDetails(
            environment.getArgument("description"),
            environment.getArgument("interval_km"),
            environment.getArgument("interval_months")
        );
    }
}
