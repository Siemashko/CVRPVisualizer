package com.siemash.cvrpvisualizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class GraphHopperRouteResponse {
    private Map<String, Object> hints;
    private Map<String, Object> info;
    private List<GraphHopperPath> paths;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphHopperPath {
        private Double distance;
        private Double time;
    }
}
