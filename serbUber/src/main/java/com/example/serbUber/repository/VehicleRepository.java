package com.example.serbUber.repository;

import com.example.serbUber.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("select v.rate from Vehicle v where v.id = ?1")
    double getVehicleRatingById(Long id);
}
