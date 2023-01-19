package com.example.serbUber.repository;

import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrivingNotificationRepository extends JpaRepository<DrivingNotification, Long> {

    @Query("select distinct dn from DrivingNotification dn left join fetch dn.sender sender " +
        "left join fetch sender.drivings left join fetch dn.route route left join fetch route.locations " +
        "left join fetch dn.receiversReviewed rs left join RegularUser regular_user on regular_user.id=key(rs).id left join regular_user.drivings " +
        "where dn.reservation=true")
    List<DrivingNotification> findAllReservation();

    @Query("select distinct dn from DrivingNotification dn left join fetch dn.sender sender " +
        "left join fetch sender.drivings left join fetch dn.route route left join fetch route.locations " +
        "left join fetch dn.receiversReviewed rs left join RegularUser regular_user on regular_user.id=key(rs).id left join regular_user.drivings " +
        "where dn.reservation=false")
    List<DrivingNotification> findAllNotReservation();
}
