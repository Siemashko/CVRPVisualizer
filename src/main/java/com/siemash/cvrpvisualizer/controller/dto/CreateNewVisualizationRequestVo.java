package com.siemash.cvrpvisualizer.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class CreateNewVisualizationRequestVo {
    private String algorithm;
    private List<Double> vehicleCapacities;
    private List<PointDto> points;
    private PointDto depot;
}
