package com.jpr.maintenance.db.repository;

import com.jpr.maintenance.db.model.TaskDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDetailsRepository extends CrudRepository<TaskDetails, Integer> {
}
