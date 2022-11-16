package com.example.serbUber.repository;
import com.example.serbUber.model.Driving;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrivingRepository extends JpaRepository<Driving, Long> {

    @Query(value = "select d from Driving d left join fetch d.route r left join fetch r.locations dest left join fetch d.usersPaid up left join fetch d.users u where u.id = ?1")
//    @Query(value="select * from drivings d, routes r, route_destinations rd, locations l, drivings_users du, regular_users ru where d.route_id = r.id and r.id = rd.route_id and rd.location_id = l.id and d.id = du.driving_id and ru.id = du.user_id and ru.email='ana@gmail.com'",nativeQuery = true)
    List<Driving> findByUserId(Long id, Pageable pageable);

    @Query(value = "select d from Driving d left join fetch d.route r left join fetch r.locations dest left join fetch d.usersPaid up left join fetch d.users u where d.driverEmail = ?1")
    List<Driving> findByDriverEmail(String email, Pageable pageable);

    @Query("select d from Driving d left join fetch d.route r left join fetch r.locations dest left join fetch d.usersPaid up left join fetch d.users u where d.id=?1")
    Optional<Driving> getDrivingById(Long id);

    @Query(value = "select distinct d from Driving d left join fetch d.route r left join fetch r.locations dest left join fetch d.users u " +
        "where d.driverEmail = ?1 and ((d.drivingStatus <> 2 and d.started > current_timestamp) or (d.active = true and d.started < current_timestamp)) order by d.started asc")
    List<Driving> findByDriverId(String id);
}
