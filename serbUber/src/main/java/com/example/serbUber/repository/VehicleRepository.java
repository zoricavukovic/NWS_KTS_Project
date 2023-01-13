package com.example.serbUber.repository;

import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("select v.rate from Vehicle v where v.id = ?1")
    double getVehicleRatingById(Long id);

    @Query("select v from Vehicle v inner join Driver driver on driver.vehicle.id = v.id where driver.active = true")
    List<Vehicle> getAllVehiclesForActiveDriver();

    @Query("select v from Vehicle v left join fetch v.vehicleTypeInfo info where info.vehicleType=?1")
    Vehicle getVehicleByType(VehicleType vehicleType);

    @Query("select distinct driver.id from Vehicle v left join Driver driver on driver.vehicle.id = v.id where v.id=?1")
    Optional<Long> getDriverIdByVehicleId(Long vehicleId);

    @Query("select distinct v from Vehicle v inner join Driver driver on driver.vehicle.id = v.id where driver.id=?1")
    Optional<Vehicle> getVehicleByDriverId(Long driverId);
}
