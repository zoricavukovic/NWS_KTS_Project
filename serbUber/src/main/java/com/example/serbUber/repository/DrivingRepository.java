package com.example.serbUber.repository;
import com.example.serbUber.model.Driving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrivingRepository extends JpaRepository<Driving, Long> {

    @Query(value = "select d from Driving d left join fetch d.route r left join fetch r.destinations dest left join fetch d.usersPaid up left join fetch d.users u where u.email = ?1 order by d.started desc")
    List<Driving> findByUserEmail(String email);

    @Query(value = "select d from Driving d left join fetch d.route r left join fetch r.destinations dest left join fetch d.usersPaid up left join fetch d.users u where d.driverEmail = ?1 order by d.started desc")
    List<Driving> findByDriverEmail(String email);

    @Query("select d from Driving d left join fetch d.route r left join fetch r.destinations dest left join fetch d.usersPaid up where d.id=?1")
    Optional<Driving> getDrivingById(Long id);
}
