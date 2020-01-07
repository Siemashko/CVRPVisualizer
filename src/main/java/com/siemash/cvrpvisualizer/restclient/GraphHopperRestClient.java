package com.siemash.cvrpvisualizer.restclient;

import com.siemash.cvrpvisualizer.model.GraphHopperRouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "graphhopper", url = "localhost:8989")
public interface GraphHopperRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/route")
    GraphHopperRouteResponse findRoute(@RequestParam("point") List<String> points);

}
