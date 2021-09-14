package com.jpr.maintenance.database.model;

import com.jpr.maintenance.validation.model.taskdetails.TaskDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ToString
@NoArgsConstructor
@Entity
@Table(name = "task_details")
@Getter
public class TaskDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer task_id;
    private String description;
    private Integer interval_km;
    private Integer interval_months;

    private TaskDetailsEntity(String description, Integer interval_km, Integer interval_months) {
        this.description = description;
        this.interval_km = interval_km;
        this.interval_months = interval_months;
    }

    public static TaskDetailsEntity of(TaskDetails taskDetails) {
        return new TaskDetailsEntity(
            taskDetails.getDescription(),
            taskDetails.getInterval_km(),
            taskDetails.getInterval_months()
        );
    }
}
