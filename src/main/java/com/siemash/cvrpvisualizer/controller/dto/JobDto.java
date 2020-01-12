package com.siemash.cvrpvisualizer.controller.dto;

import com.siemash.cvrpvisualizer.model.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {

    private Long id;
    private String algorithm;
    private String jobStatus;
    private List<Double> vehicleCapacities;
    private List<PointDto> points;
    private PointDto depot;
    private String jobResult;

    public static JobDto from(Job source) {
        return new JobDto(
                source.getId(),
                source.getAlgorithm().toString(),
                source.getStatus().toString(),
                source.getVehicleCapacities(),
                source.getPoints().stream().map(PointDto::from).collect(Collectors.toList()),
                PointDto.from(source.getDepot()),
                source.getJobResult()
        );
    }
}
