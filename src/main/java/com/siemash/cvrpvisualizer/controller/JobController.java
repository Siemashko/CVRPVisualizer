package com.siemash.cvrpvisualizer.controller;

import com.siemash.cvrpvisualizer.controller.dto.CreateNewVisualizationRequestVo;
import com.siemash.cvrpvisualizer.controller.dto.JobDto;
import com.siemash.cvrpvisualizer.exception.NotFoundException;
import com.siemash.cvrpvisualizer.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public List<JobDto> getAllJobs() {
        return jobService.findAll().stream().map(JobDto::from).collect(Collectors.toList());
    }

    @GetMapping("/{jobId}")
    public JobDto getJobById(@PathVariable Long jobId) throws NotFoundException {
        return JobDto.from(jobService.findById(jobId));
    }

    @PostMapping
    public JobDto createNewVisualization(@RequestBody CreateNewVisualizationRequestVo request){
        return JobDto.from(jobService.createNewVisualization(request));
    }

}
