package com.example.serbUber.repository;

import com.example.serbUber.model.DrivingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingNotificationRepository extends JpaRepository<DrivingNotification, Long> {
}
