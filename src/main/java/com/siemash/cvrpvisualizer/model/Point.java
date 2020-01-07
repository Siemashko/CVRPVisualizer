package com.siemash.cvrpvisualizer.model;

import com.siemash.cvrpvisualizer.controller.dto.PointDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double latitude;
    private Double longitude;
    private Double weight;

    public String toGHRequestFormat(){
        return this.latitude+","+this.longitude;
    }

    Point(Double latitude, Double longitude, Double weight) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.weight = weight;
    }

    public static Point fromDto(PointDto source) {
        return new Point(source.getLatitude(), source.getLongitude(), source.getWeight());
    }

    @Override
    public String toString() {
        return "{\"latitude\"="+this.latitude+",\"longitude\"="+this.longitude+"}";
    }
}
