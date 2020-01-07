package com.siemash.cvrpvisualizer.service;

import com.siemash.cvrpvisualizer.model.DistanceMatrix;
import com.siemash.cvrpvisualizer.model.DistanceMatrix.PointPair;
import com.siemash.cvrpvisualizer.model.GraphHopperRouteResponse;
import com.siemash.cvrpvisualizer.model.Point;
import com.siemash.cvrpvisualizer.restclient.GraphHopperRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistanceService {

    private final GraphHopperRestClient graphHopperRestClient;

    @PostConstruct
    public void testGraphHopper() {
        final Point point1 = new Point(1L,52.269838, 20.853006, 0.0);
        final Point point2 = new Point(2L, 52.251346, 21.157876, 0.0);
        final GraphHopperRouteResponse response = graphHopperRestClient.findRoute(List.of(point1.toGHRequestFormat(), point2.toGHRequestFormat()));
        log.info("{}", response);
    }

    public DistanceMatrix convertPointsToDistanceMatrix(List<Point> points) {

        final Map<PointPair, Double> distanceMap = new HashMap<>();
        List<PointPair> pointPairs = points.stream().flatMap(point1 -> points.stream().map(point2 -> new PointPair(point1,point2)))
                .collect(
                Collectors.toList());

        pointPairs.forEach(pointPair -> {
            final GraphHopperRouteResponse response = graphHopperRestClient.findRoute(List.of(pointPair.getPoint1().toGHRequestFormat(), pointPair.getPoint2().toGHRequestFormat()));
            distanceMap.put(pointPair, response.getPaths().get(0).getDistance());
        });

        final DistanceMatrix distanceMatrix = DistanceMatrix.builder().distanceMap(distanceMap).build();

        return distanceMatrix;
    }
}
