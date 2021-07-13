package com.jpr.graphql.maintenance.db.model;

import graphql.schema.DataFetchingEnvironment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task_details")
public class TaskDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer task_id;
    private String description;
    private int interval_km;
    private int interval_months;

    public TaskDetails(){}

    private TaskDetails(String description, int interval_km, int interval_months) {
        this.description = description;
        this.interval_km = interval_km;
        this.interval_months = interval_months;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInterval_km() {
        return interval_km;
    }

    public void setInterval_km(int interval_km) {
        this.interval_km = interval_km;
    }

    public int getInterval_months() {
        return interval_months;
    }

    public void setInterval_months(int interval_months) {
        this.interval_months = interval_months;
    }

    public static TaskDetails of (DataFetchingEnvironment environment) {
        return new TaskDetails(
            environment.getArgument("description"),
            environment.getArgument("interval_km"),
            environment.getArgument("interval_months")
        );
    }
}
