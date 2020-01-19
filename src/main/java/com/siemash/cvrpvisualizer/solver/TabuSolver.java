package com.siemash.cvrpvisualizer.solver;

import com.siemash.cvrpvisualizer.exception.UnsolvableJobException;
import com.siemash.cvrpvisualizer.model.DistanceMatrix;
import com.siemash.cvrpvisualizer.model.DistanceMatrix.PointPair;
import com.siemash.cvrpvisualizer.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabuSolver extends Solver{

    private List<Double> previousSolutions;
    private Integer tabuHorizon = 10;
    private Double bestSolutionCost;
    private Integer currentIteration;
    private Integer maxIterations;
    private Map<PointPair, Integer> tabuMatrix;

    public TabuSolver(List<Double> vehicleCapacities,
                      List<Point> points,
                      Point startingPoint,
                      DistanceMatrix distanceMatrix,
                      Integer maxIterations) throws UnsolvableJobException {
        super(vehicleCapacities, points, startingPoint, distanceMatrix);
        final GreedySolver solver = new GreedySolver(vehicleCapacities, points, startingPoint, distanceMatrix);
        while (!solver.isSolved()) {
            solver.nextStep();
        }
        super.vehicleRoutes = solver.getVehicleRoutes();
        super.vehicleLoads = solver.getVehicleLoads();

        this.previousSolutions = new ArrayList<>();
        this.previousSolutions.add(solver.getSolutionCost());
        this.currentIteration = 0;
        this.maxIterations = maxIterations;
        this.bestSolutionCost = super.getSolutionCost();

        this.tabuMatrix =  new HashMap<>();
        Stream.concat(Stream.of(startingPoint), points.stream()).flatMap(point1 -> Stream.concat(Stream.of(startingPoint), points.stream()).map(point2 -> new DistanceMatrix.PointPair(point1,point2)))
                .forEach(pointPair -> tabuMatrix.put(pointPair, 0));
    }

    public Solver nextStep() {
        if (this.currentIteration < this.maxIterations) {
            List<List<Point>> vehicleRoutesDeepCopy = this.vehicleRoutes.stream().map(ArrayList::new).collect(Collectors.toList());
            List<Double> vehicleLoadsDeepCopy = new ArrayList<>(List.copyOf(super.vehicleLoads));
            this.currentIteration++;
            Double bestNeighborhoodCost = Double.MAX_VALUE;
            Integer swapIndexA = -1;
            Integer swapIndexB = -1;
            Integer swapRouteFromIndex = -1;
            Integer swapRouteToIndex = -1;
            for (int vehicleRouteFromIndex = 0; vehicleRouteFromIndex < vehicleRoutesDeepCopy.size(); vehicleRouteFromIndex++) {
                List<Point> vehicleRouteFrom =  vehicleRoutesDeepCopy.get(vehicleRouteFromIndex);
                int vehicleRouteFromLength = vehicleRouteFrom.size();
                for (int i = 1; i < vehicleRouteFromLength-1; i++) {

                    for (int vehicleRouteToIndex = 0; vehicleRouteToIndex < vehicleRoutesDeepCopy
                            .size(); vehicleRouteToIndex++) {
                        List<Point> vehicleRouteTo = vehicleRoutesDeepCopy.get(vehicleRouteToIndex);
                        int vehicleRouteToLength = vehicleRouteTo.size();

                        for (int j = 0; j < vehicleRouteToLength - 1; j++) {
                            if ((vehicleRouteFromIndex == vehicleRouteToIndex) || this.vehicleLoads
                                    .get(vehicleRouteToIndex) + vehicleRouteFrom.get(i).getWeight() <= this.vehicleCapacities
                                    .get(vehicleRouteToIndex)) {
                                if (!((vehicleRouteFromIndex == vehicleRouteToIndex) && ((j == i) || (j == i - 1)))) {  // Not a move that Changes solution cost
                                    Double minusCost1 = distanceMatrix
                                            .get(vehicleRouteFrom.get(i - 1), vehicleRouteFrom.get(i));
                                    Double minusCost2 = distanceMatrix
                                            .get(vehicleRouteFrom.get(i), vehicleRouteFrom.get(i + 1));
                                    Double minusCost3 = distanceMatrix
                                            .get(vehicleRouteTo.get(j), vehicleRouteTo.get(j + 1));

                                    Double addedCost1 = distanceMatrix
                                            .get(vehicleRouteFrom.get(i - 1), vehicleRouteFrom.get(i + 1));
                                    Double addedCost2 = distanceMatrix
                                            .get(vehicleRouteTo.get(j), vehicleRouteFrom.get(i));
                                    Double addedCost3 = distanceMatrix
                                            .get(vehicleRouteFrom.get(i), vehicleRouteTo.get(j + 1));
                                    if ((tabuMatrix.get(new PointPair(vehicleRouteFrom.get(i - 1),vehicleRouteFrom.get(i+1))) != 0)
                                            || (tabuMatrix.get(new PointPair(vehicleRouteTo.get(j),vehicleRouteFrom.get(i))) != 0)
                                            || (tabuMatrix.get(new PointPair(vehicleRouteFrom.get(i),vehicleRouteTo.get(j+1))) != 0)) {
                                        break;
                                    }

                                    Double neighborhoodCost = addedCost1 + addedCost2 + addedCost3 - minusCost1 - minusCost2 - minusCost3;

                                    if (neighborhoodCost < bestNeighborhoodCost) {
                                        bestNeighborhoodCost = neighborhoodCost;
                                        swapIndexA = i;
                                        swapIndexB = j;
                                        swapRouteFromIndex = vehicleRouteFromIndex;
                                        swapRouteToIndex = vehicleRouteToIndex;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            super.points.stream().flatMap(point1 -> super.points.stream().map(point2 -> new DistanceMatrix.PointPair(point1,point2)))
                    .filter(pointPair -> tabuMatrix.get(pointPair) > 0)
                    .forEach(pointPair -> tabuMatrix.put(pointPair, tabuMatrix.get(pointPair)-1));

            List<Point> vehicleRouteFrom = new ArrayList<>(vehicleRoutesDeepCopy.get(swapRouteFromIndex));
            List<Point> vehicleRouteTo = new ArrayList<>(vehicleRoutesDeepCopy.get(swapRouteToIndex));
            vehicleRoutesDeepCopy.set(swapRouteFromIndex, new ArrayList<>());
            vehicleRoutesDeepCopy.set(swapRouteToIndex, new ArrayList<>());


            Point swapPoint = vehicleRouteFrom.get(swapIndexA);

            Point pointBefore = vehicleRouteFrom.get(swapIndexA-1);
            Point pointAfter = swapIndexA + 1 < vehicleRouteFrom.size() ? vehicleRouteFrom.get(swapIndexA+1) : super.startingPoint;
            Point pointF = vehicleRouteTo.get(swapIndexB);
            Point pointG = swapIndexB + 1 < vehicleRouteTo.size() ? vehicleRouteTo.get(swapIndexB+1) : super.startingPoint;

            Random tabuRandomizer = new Random();
            int randomDelay1 = tabuRandomizer.nextInt(5);
            int randomDelay2 = tabuRandomizer.nextInt(5);
            int randomDelay3 = tabuRandomizer.nextInt(5);

            this.tabuMatrix.put(new PointPair(pointBefore,swapPoint), this.tabuHorizon + randomDelay1);
            this.tabuMatrix.put(new PointPair(swapPoint,pointAfter), this.tabuHorizon + randomDelay2);
            this.tabuMatrix.put(new PointPair(pointF,pointG), this.tabuHorizon + randomDelay3);

            vehicleRouteFrom.remove((int) swapIndexA);

            if (swapRouteFromIndex.equals(swapRouteToIndex)) {
                if (swapIndexA < swapIndexB) {
                    vehicleRouteTo.add(swapIndexB, swapPoint);
                } else {
                    vehicleRouteTo.add(swapIndexB + 1, swapPoint);
                }
            } else {
                vehicleRouteTo.add(swapIndexB + 1, swapPoint);
            }


            vehicleRoutesDeepCopy.set(swapRouteFromIndex, vehicleRouteFrom);
            vehicleLoadsDeepCopy.set(swapRouteFromIndex,vehicleLoadsDeepCopy.get(swapRouteFromIndex) - swapPoint.getWeight());

            vehicleRoutesDeepCopy.set(swapRouteToIndex, vehicleRouteTo);
            vehicleLoadsDeepCopy.set(swapRouteToIndex,vehicleLoadsDeepCopy.get(swapRouteToIndex) + swapPoint.getWeight());

            this.previousSolutions.add(super.getSolutionCost());

            if (Solver.calculateSolutionCost(super.distanceMatrix, vehicleRoutesDeepCopy) < this.bestSolutionCost) {
                this.bestSolutionCost = Solver.calculateSolutionCost(super.distanceMatrix, vehicleRoutesDeepCopy);
                super.vehicleRoutes = vehicleRoutesDeepCopy;
                super.vehicleLoads = vehicleLoadsDeepCopy;
            }
        }
        return this;
    }

    public boolean isTerminated() {
        return this.currentIteration >= this.maxIterations;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
