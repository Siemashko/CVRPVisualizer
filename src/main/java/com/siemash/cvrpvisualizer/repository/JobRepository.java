package com.siemash.cvrpvisualizer.repository;

import com.siemash.cvrpvisualizer.model.Job;
import com.siemash.cvrpvisualizer.model.Job.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByStatusIn(JobStatus... jobStatuses);
}
