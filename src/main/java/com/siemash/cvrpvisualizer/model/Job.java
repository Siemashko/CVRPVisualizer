package com.siemash.cvrpvisualizer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Enumerated(EnumType.STRING)
    private Algorithm algorithm;

    @ElementCollection(fetch=FetchType.EAGER)
    private List<Double> vehicleCapacities;

    @ElementCollection(fetch=FetchType.EAGER)
    private Set<Point> points;

    @OneToOne(fetch=FetchType.EAGER)
    private Point depot;

    @Column(columnDefinition = "text")
    private String jobResult;

    public enum JobStatus {
        NEW, IN_PROGRESS, DONE, ERROR
    }

    public enum Algorithm {
         GREEDY, TABU
    }
}
