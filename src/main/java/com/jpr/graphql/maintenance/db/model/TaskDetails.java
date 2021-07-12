package com.jpr.graphql.maintenance.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task_details")
public class TaskDetails {

    @Id
    @GeneratedValue()
    private int task_id;
    private String description;
    private int interval_km;
    private int interval_months;

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
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
}
