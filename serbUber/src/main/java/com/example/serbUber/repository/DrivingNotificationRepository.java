package com.example.serbUber.repository;

import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrivingNotificationRepository extends JpaRepository<DrivingNotification, Long> {

    @Query("select dn from DrivingNotification dn left join fetch dn.receiversReviewed rs")
    List<DrivingNotification> findAll();
}
