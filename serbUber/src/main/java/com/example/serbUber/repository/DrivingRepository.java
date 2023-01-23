package com.example.serbUber.repository;
import com.example.serbUber.model.Driving;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrivingRepository extends JpaRepository<Driving, Long> {

   // @Query(value = "select d from Driving d left join fetch d.route r left join fetch r.locations dest left join fetch d.usersPaid up left join fetch d.users u where u.id = ?1")
   @Query(value = "select * from drivings d, drivings_users du where d.id = du.driving_id and du.user_id=?1 and (d.driving_status=3 or d.driving_status=4)", nativeQuery = true)
    Page<Driving> findByUserId(final Long id, final Pageable pageable);

   @Query(value="select * from drivings d, drivings_users du where d.id = du.driving_id and du.user_id=?1 and (d.driving_status=3 or d.driving_status=4)", nativeQuery = true)
   List<Driving> getDrivingsForUserId(final Long id);

//    @Query(value = "select d from Driving d left join fetch d.route r left join fetch r.locations dest left join fetch d.usersPaid up left join fetch d.users u where d.driverId = ?1 order by dest.id")
    @Query(value = "select * from drivings d where d.driver_id=?1 and (d.driving_status=3 or d.driving_status=4)", nativeQuery = true)
    Page<Driving> findByDriverId(final Long id, final Pageable pageable);

    @Query("select d from Driving d left join fetch d.route r left join fetch d.driver left join fetch r.locations dest left join fetch d.users u where d.id=?1 order by dest.id")
    Optional<Driving> getDrivingById(final Long id);

    @Query(value = "select distinct d from Driving d left join fetch d.driver driver left join fetch d.route r left join fetch r.locations dest left join fetch d.users u " +
        "where driver.id = ?1 and ((d.drivingStatus = 2 and d.started > current_timestamp) or (d.active = true and d.started < current_timestamp and d.drivingStatus <> 5)) order by d.started asc")
    List<Driving> getAllNowAndFutureDrivings(final Long id);

    @Query("select d from Driving d left join fetch d.driver driver where driver.id=?1 and d.active = true")
    Optional<Driving> getActiveDrivingForDriver(final Long driverId);

    @Query("select distinct d from Driving d left join fetch d.driver driver inner join d.users user where user.id=?1 and ((d.active = true or (d.drivingStatus=2 and d.end is null)) or (d.active = false and d.drivingStatus=5)) and driver.id is not null order by d.started asc")
    List<Driving> getActiveDrivingForUser(final Long userId, final LocalDateTime limitDateTime);

    @Query(value = "select distinct d from Driving d left join fetch d.driver driver left join fetch d.route r left join fetch r.locations dest left join fetch d.users u " +
        "where driver.id=?1 and d.drivingStatus = 2 and d.started > current_timestamp order by d.started asc")
    List<Driving> driverHasFutureDriving(final Long id);

    @Query("select distinct d.id from Driving d where d.route.id = ?1")
    Optional<Long> findDrivingByFavouriteRoute(final Long routeId);

    @Query("select d from Driving d left join d.users u where u.email = ?1")
    List<Driving> getAllDrivingsForUserEmail(final String email);

    //ako se promeni enum mora se ovde promeniti!
    @Query("select d from Driving d left join fetch d.driver driver where driver.id=?1 and d.drivingStatus=3")
    List<Driving> getFinishedDrivingsForDriver(final Long id);

    @Query("select d from Driving d inner join d.users user where user.id=?1 and d.drivingStatus=3")
    List<Driving> getFinishedDrivingsForRegular(final Long id);

    @Query("select d from Driving d where d.drivingStatus=3")
    List<Driving> getAllFinishedDrivings();

    @Query("select d from Driving d where d.reservation=true")
    List<Driving> getAllReservations();

    @Query("select d from Driving d left join fetch d.route r left join fetch d.driver driver where d.drivingStatus=5 and driver.id=?1 and d.active=true")
    Driving getOnWayToDepartureDriving(Long driverId);
}
