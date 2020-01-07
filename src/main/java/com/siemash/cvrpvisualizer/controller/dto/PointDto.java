package com.siemash.cvrpvisualizer.controller.dto;

import com.siemash.cvrpvisualizer.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    private Double latitude;
    private Double longitude;
    private Double weight;

    public static PointDto from(Point source) {
        return new PointDto(source.getLatitude(), source.getLongitude(), source.getWeight());
    }
}
