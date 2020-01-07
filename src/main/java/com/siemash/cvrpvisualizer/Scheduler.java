package com.siemash.cvrpvisualizer;

import com.siemash.cvrpvisualizer.model.Job;
import com.siemash.cvrpvisualizer.model.Job.JobStatus;
import com.siemash.cvrpvisualizer.service.JobService;
import com.siemash.cvrpvisualizer.service.ProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Scheduler {

    private final JobService jobService;
    private final ProcessorService processorService;

    @Scheduled(fixedDelay = 10_000L)
    public void runNewJobs() {
        final List<Job> newJobs = jobService.findByStatusIn(JobStatus.NEW);
        newJobs.stream().limit(3).forEach(processorService::processJob);
    }

}
