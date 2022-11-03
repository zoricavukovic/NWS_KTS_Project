package com.example.serbUber.repository;
import com.example.serbUber.model.Driving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingRepository extends JpaRepository<Driving, Long> {
}
