package com.siemash.cvrpvisualizer.solver;

import com.siemash.cvrpvisualizer.exception.UnsolvableJobException;
import com.siemash.cvrpvisualizer.model.DistanceMatrix;
import com.siemash.cvrpvisualizer.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GreedySolver extends Solver {

    private Integer currentVehicleIndex;

    public GreedySolver(List<Double> vehicleCapacities,
                        List<Point> points,
                        Point startingPoint,
                        DistanceMatrix distanceMatrix) {
        super(vehicleCapacities, points, startingPoint, distanceMatrix);
        this.currentVehicleIndex = 0;
    }

    public Solver nextStep() throws UnsolvableJobException {
        if (!super.isSolved()) {
            final List<Point> possiblePointsForCurrentVehicle = super.points.stream()
                    .filter(point -> !super.isPointAssignedMap.get(point))
                    .filter(point -> super.vehicleLoads.get(currentVehicleIndex) + point
                            .getWeight() <= super.vehicleCapacities.get(currentVehicleIndex))
                    .collect(Collectors.toList());

            if (possiblePointsForCurrentVehicle.isEmpty()) {
                this.currentVehicleIndex++;
                if (currentVehicleIndex >= super.numberOfVehicles) {
                    throw new UnsolvableJobException("Cannot solve the CVRP with greedy algorithm for the given set of parameters");
                }
                return this;
            }

            final Point closestPoint = possiblePointsForCurrentVehicle.stream().min(Comparator.comparing(
                    point -> super.distanceMatrix.get(super.currentVehiclePosition.get(currentVehicleIndex), point)))
                    .get();

            super.vehicleLoads.set(this.currentVehicleIndex,super.vehicleLoads.get(this.currentVehicleIndex) + closestPoint.getWeight());
            super.vehicleRoutes.get(this.currentVehicleIndex).add(closestPoint);
            super.currentVehiclePosition.set(this.currentVehicleIndex, closestPoint);
            super.isPointAssignedMap.put(closestPoint, true);

            if(super.isSolved()) {
                super.currentVehiclePosition.set(this.currentVehicleIndex, startingPoint);
                for(List<Point> vehicleRoutes : super.vehicleRoutes) {
                    vehicleRoutes.add(startingPoint);
                }
            }

            return this;
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
