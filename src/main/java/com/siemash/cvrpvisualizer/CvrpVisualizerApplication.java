package com.siemash.cvrpvisualizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class CvrpVisualizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvrpVisualizerApplication.class, args);
    }

}
