package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> getDriverByEmail(String email);

    Optional<Driver> getDriverById(Long id);

}
