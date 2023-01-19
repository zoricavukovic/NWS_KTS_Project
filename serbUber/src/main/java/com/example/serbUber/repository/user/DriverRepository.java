package com.example.serbUber.repository.user;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    //1. vozac je trenutno slobodan, voznja: a) zavrsena u proslosti or b) treba da se realizuje u buducnosti nakon ove voznje
    @Query("select d from Driver d left join fetch d.drivings dr left join fetch d.vehicle v left join fetch v.vehicleTypeInfo typeInfo " +
        "where d.active=true and d.drive = false and v.babySeat=?4 and v.petFriendly=?5 and typeInfo.vehicleType=?3 " +
        "and (dr.drivingStatus<>2 or (dr.drivingStatus=2 and ((?1>dr.started and ?1>dr.end) or (?2<dr.started))))")
    List<Driver> getActiveAndFreeDrivers(
        final LocalDateTime start,
        final LocalDateTime end,
        final VehicleType vehicleType,
        final boolean babySeat,
        final boolean petFriendly
    );

    //2. vozac trenutno vozi, bice slobodan nakon te voznje i brzo ce zavrsiti voznju
    @Query("select d from Driver d left join fetch d.drivings dr left join fetch d.vehicle vehicle left join fetch vehicle.vehicleTypeInfo info where d.active=true " +
        "and d.drive = true and dr.active = false and dr.started < ?1")
    List<Driver> getBusyDriversNow(LocalDateTime start, VehicleType vehicleType);

    @Query("select distinct d from Driver d left join fetch d.drivings dr inner join dr.route route on route.id=dr.route.id  left join fetch d.vehicle v left join fetch v.vehicleTypeInfo typeInfo " +
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
}
