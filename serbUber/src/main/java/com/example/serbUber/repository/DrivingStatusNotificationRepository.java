package com.example.serbUber.repository;

import com.example.serbUber.model.DrivingStatusNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingStatusNotificationRepository extends JpaRepository<DrivingStatusNotification, Long> {
}
