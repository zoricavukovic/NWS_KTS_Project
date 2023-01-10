package com.example.serbUber.repository;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingWithVehicleRepository extends JpaRepository<Driving,Long> {
}
