package com.siemash.cvrpvisualizer.solver;

import com.siemash.cvrpvisualizer.model.DistanceMatrix;
import com.siemash.cvrpvisualizer.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabuSolver extends Solver{

    private Integer currentVehicleIndex;

    public TabuSolver (List<Double> vehicleCapacities,
                        List<Point> points,
                        Point startingPoint,
                        DistanceMatrix distanceMatrix) {
        super(vehicleCapacities, points, startingPoint, distanceMatrix);
        this.currentVehicleIndex = 0;
    }
}
