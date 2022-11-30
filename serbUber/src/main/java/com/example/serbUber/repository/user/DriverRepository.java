package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d left join fetch d.drivings dr left join fetch d.vehicle v left join fetch d.currentLocation l where d.email=?1")
    Optional<Driver> getDriverByEmail(String email);

    @Query("select d from Driver d left join fetch d.drivings dr left join fetch d.vehicle v left join fetch d.currentLocation l where d.id=?1")
    Optional<Driver> getDriverById(Long id);

    @Query("select d.rate from Driver d where d.id = ?1")
    double getRatingForDriver(Long id);

    @Query("select distinct d from Driver d left join fetch d.drivings dr")
    List<Driver> getAllWithDrivings();

    @Query("select d from Driver d left join fetch d.drivings drivings where d.active = true and drivings.active = false")
    List<Driver> getActiveAndFreeDrivers();

    @Query("select d from Driver d left join fetch d.drivings drivings left join fetch d.vehicle v where d.verified = true")
    List<Driver> findAllVerified();
}
