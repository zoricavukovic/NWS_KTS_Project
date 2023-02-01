package com.example.serbUber.repository.user;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d left join fetch d.vehicle v left join fetch d.drivings dr where d.id=?1")
    Optional<Driver> getDriverById(Long id);

    @Query("select d from Driver d where d.id=?1")
    Optional<Driver> getDriverByIdWithoutDrivings(Long id);

    @Query("select d.rate from Driver d where d.id = ?1")
    double getRatingForDriver(Long id);

    @Query("select distinct d from Driver d left join fetch d.drivings dr left join fetch d.vehicle v left join fetch v.vehicleTypeInfo typeInfo " +
        "where d.active=true and typeInfo.vehicleType=?1")
    List<Driver> getActiveDriversWhichVehicleMatchParams(final VehicleType vehicleType);

    @Query("select distinct d from Driver d left join fetch d.drivings dr")
    List<Driver> getAllWithDrivings();

    @Query("select distinct d from Driver d left join fetch d.drivings drivings left join fetch d.vehicle v where d.verified = true")
    List<Driver> findAllVerified();

    @Query("select d.blocked from Driver d where d.id = ?1")
    boolean getIsBlocked(Long id);

    @Modifying
    @Query("update Driver d set d.rate = :rate where d.id = :id")
    void updateDrivingRate(Long id, double rate);

    @Query("select d from Driver d left join fetch d.vehicle v where d.email=?1")
    Optional<Driver> getDriverByEmail(String email);

    @Query("select distinct d from Driver d left join fetch d.drivings dr where d.active=true")
    List<Driver> getActiveDrivers();
}
