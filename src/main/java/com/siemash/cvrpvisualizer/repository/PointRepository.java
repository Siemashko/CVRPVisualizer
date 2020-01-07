package com.siemash.cvrpvisualizer.repository;

import com.siemash.cvrpvisualizer.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
}
