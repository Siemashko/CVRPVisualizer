package com.siemash.cvrpvisualizer.service;

import com.siemash.cvrpvisualizer.controller.dto.CreateNewVisualizationRequestCommand;
import com.siemash.cvrpvisualizer.exception.NotFoundException;
import com.siemash.cvrpvisualizer.model.Job;
import com.siemash.cvrpvisualizer.model.Job.*;
import com.siemash.cvrpvisualizer.model.Point;
import com.siemash.cvrpvisualizer.repository.JobRepository;
import com.siemash.cvrpvisualizer.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PointRepository pointRepository;

    public Job createNewVisualization(CreateNewVisualizationRequestCommand request) {

        final Point depot = Point.fromDto(request.getDepot());

        final List<Point> points = request.getPoints().stream().map(Point::fromDto).collect(Collectors.toList());

        pointRepository.saveAll(points);
        pointRepository.save(depot);
        pointRepository.flush();

        final Job newJob = Job.builder()
                .vehicleCapacities(request.getVehicleCapacities())
                .depot(depot)
                .points(Set.copyOf(points))
                .algorithm(Algorithm.valueOf(request.getAlgorithm()))
                .status(JobStatus.NEW)
                .build();


        return jobRepository.saveAndFlush(newJob);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job findById(Long jobId) throws NotFoundException {
        return jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Could not find job with id " + jobId));
    }

    public List<Job> findByStatusIn(JobStatus... jobStatuses) {
        return jobRepository.findAllByStatusIn(jobStatuses);
    }
}
