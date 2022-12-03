package com.example.serbUber.repository;

import com.example.serbUber.model.DrivingLocationIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingLocationIndexRepository extends JpaRepository<DrivingLocationIndex, Long> {
}
