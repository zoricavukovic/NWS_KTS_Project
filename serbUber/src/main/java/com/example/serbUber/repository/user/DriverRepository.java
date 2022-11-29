package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d left join fetch d.drivings dr left join fetch d.vehicle v left join fetch d.currentLocation l where d.email=?1")
    Optional<Driver> getDriverByEmail(String email);

    Optional<Driver> getDriverById(Long id);

    @Query("select d.rate from Driver d where d.id = ?1")
    double getRatingForDriver(Long id);

    //1. vozac je trenutno slobodan, voznja: a) zavrsena u proslosti or b) treba da se realizuje u buducnosti nakon ove voznje
    @Query("select d from Driver d left join fetch d.drivings dr where d.active=true and dr.driverId = d.id and " +
            "d.drive = false and dr.active = false and (dr.started < ?1 or dr.started > ?2)")
    List<Driver> getActiveAndFreeDrivers(LocalDateTime start, LocalDateTime end);

    //2. vozac trenutno vozi, bice slobodan nakon te voznje i brzo ce zavrsiti voznju
    @Query("select d from Driver d left join fetch d.drivings dr where d.active=true and dr.driverId = d.id " +
            "and d.drive = true and dr.active = false and dr.started < ?1")
    List<Driver> getBusyDriversNow(LocalDateTime start);

     // @Query(value="select distinct d from drivers d, drivings dr where dr.driver_id = d.id and d.active = true and dr.active = false and (?2 + ?1 * interval '1 minute' < dr.started or dr.started < ?2)", nativeQuery = true)
    // List<Driver> getOccupiedDriversWithoutFutureDrivings(LocalDateTime date);
}
