package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.TaskDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDetailsRepository extends CrudRepository<TaskDetailsEntity, Long> {
}
