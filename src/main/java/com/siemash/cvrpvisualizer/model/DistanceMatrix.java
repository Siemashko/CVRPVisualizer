package com.siemash.cvrpvisualizer.model;

import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistanceMatrix {

    private Map<PointPair, Double> distanceMap;

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PointPair {
        private Point point1;
        private Point point2;
    }

    public Double get(Point point1, Point point2) {
        return distanceMap.get(new PointPair(point1, point2));
    }

}
