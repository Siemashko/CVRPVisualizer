package com.siemash.cvrpvisualizer.solver;

import com.siemash.cvrpvisualizer.model.DistanceMatrix;
import com.siemash.cvrpvisualizer.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Solver {
    protected Integer numberOfVehicles;
    protected List<Double> vehicleCapacities;
    protected List<Point> points;
    protected Point startingPoint;
    protected DistanceMatrix distanceMatrix;
    protected List<Double> vehicleLoads;
    protected List<List<Point>> vehicleRoutes;
    protected List<Point> currentVehiclePosition;
    protected Map<Point,Boolean> isPointAssignedMap;

    public Solver(List<Double> vehicleCapacities,
                  List<Point> points,
                  Point startingPoint,
                  DistanceMatrix distanceMatrix) {
        this.numberOfVehicles = vehicleCapacities.size();
        this.vehicleCapacities = vehicleCapacities;
        this.points = points;
        this.startingPoint = startingPoint;
        this.distanceMatrix = distanceMatrix;
        this.vehicleLoads = DoubleStream.generate(() -> 0.0)
                .limit(vehicleCapacities.size())
                .boxed()
                .collect(Collectors.toList());
        this.vehicleRoutes = Stream.generate(() -> new LinkedList<Point>())
                .limit(vehicleCapacities.size())
                .collect(Collectors.toList());
        for (List<Point> vehicleRoute : this.vehicleRoutes) {
            vehicleRoute.add(this.startingPoint);
        }
        this.currentVehiclePosition = Stream.generate(() -> startingPoint)
                .limit(vehicleCapacities.size()).collect(
                        Collectors.toList());
        this.isPointAssignedMap = points.stream().collect(Collectors.toMap(Function.identity(), point -> false));

    }

    public boolean isSolved() {
        return !this.isPointAssignedMap.containsValue(false);
    }
}