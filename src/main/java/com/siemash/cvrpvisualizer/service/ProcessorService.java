package com.siemash.cvrpvisualizer.service;

import com.siemash.cvrpvisualizer.exception.UnsolvableJobException;
import com.siemash.cvrpvisualizer.model.DistanceMatrix;
import com.siemash.cvrpvisualizer.model.Job;
import com.siemash.cvrpvisualizer.model.Point;
import com.siemash.cvrpvisualizer.repository.JobRepository;
import com.siemash.cvrpvisualizer.solver.GreedySolver;
import com.siemash.cvrpvisualizer.solver.Solver;
import com.siemash.cvrpvisualizer.solver.TabuSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final JobRepository jobRepository;

    private final DistanceService distanceService;

    private static Integer MAX_ITERATIONS = 200;

    public void processJob(Job job) {
        job.setStatus(Job.JobStatus.IN_PROGRESS);
        jobRepository.save(job);
        switch(job.getAlgorithm()) {
            case GREEDY:
                processWithGreedyAlgorithm(job);
                return;
            case TABU:
                processWithTabuAlgorithm(job);
                return;
            default:
                return;
        }
    }

    private void processWithGreedyAlgorithm(Job job) {
        final List<Point> points = List.copyOf(job.getPoints());
        final Point startingPoint = job.getDepot();
        final List<Double> vehicleCapacities = job.getVehicleCapacities();
        final List<Point> pointsForDistanceMatrix = Stream.concat(Stream.of(startingPoint), points.stream())
                .collect(Collectors.toList());
        final List<String> solverStates = new ArrayList<>();

        try {
            final DistanceMatrix distanceMatrix = distanceService.convertPointsToDistanceMatrix(pointsForDistanceMatrix);
            final GreedySolver solver = new GreedySolver(vehicleCapacities, points, startingPoint, distanceMatrix);
            while (!solver.isSolved()) {
                solver.nextStep();
                solverStates.add(solver.toString());
            }
        } catch(UnsolvableJobException ex) {
            job.setStatus(Job.JobStatus.ERROR);
            job.setJobResult(ex.getMessage());
            jobRepository.saveAndFlush(job);
            return;
        } catch(Exception ex) {
            job.setStatus(Job.JobStatus.ERROR);
            job.setJobResult(ex.getMessage());
            jobRepository.saveAndFlush(job);
            return;
        }
        final String jobResult = solverStates.toString();
        job.setJobResult(jobResult);
        job.setStatus(Job.JobStatus.DONE);
        jobRepository.saveAndFlush(job);
    }

    private void processWithTabuAlgorithm(Job job) {
        final List<Point> points = List.copyOf(job.getPoints());
        final Point startingPoint = job.getDepot();
        final List<Double> vehicleCapacities = job.getVehicleCapacities();
        final List<Point> pointsForDistanceMatrix = Stream.concat(Stream.of(startingPoint), points.stream())
                .collect(Collectors.toList());
        final List<String> solverStates = new ArrayList<>();

        try {
            final DistanceMatrix distanceMatrix = distanceService.convertPointsToDistanceMatrix(pointsForDistanceMatrix);
            final TabuSolver solver = new TabuSolver(vehicleCapacities, points, startingPoint, distanceMatrix, MAX_ITERATIONS);
            solverStates.add(solver.toString());
            while (!solver.isTerminated()) {
                solver.nextStep();
                solverStates.add(solver.toString());
            }
        } catch(UnsolvableJobException ex) {
            job.setStatus(Job.JobStatus.ERROR);
            job.setJobResult(ex.getMessage());
            jobRepository.saveAndFlush(job);
            return;
        } catch(Exception ex) {
            System.out.println(ex);
            job.setStatus(Job.JobStatus.ERROR);
            job.setJobResult(ex.getMessage());
            jobRepository.saveAndFlush(job);
            throw ex;
//            return;
        }
        final String jobResult = solverStates.toString();
        job.setJobResult(jobResult);
        job.setStatus(Job.JobStatus.DONE);
        jobRepository.saveAndFlush(job);
    }
}
