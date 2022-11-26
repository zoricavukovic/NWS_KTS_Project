package com.example.serbUber.repository;

import com.example.serbUber.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("select v.rate from Vehicle v where v.id = ?1")
    double getVehicleRatingById(Long id);

    @Query("select v from Vehicle v left join fetch v.activeRoute r inner join Driver driver on driver.vehicle.id = v.id where driver.active = true")
    List<Vehicle> getAllVehiclesForActiveDriver();
}
