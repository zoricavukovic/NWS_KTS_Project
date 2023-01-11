package com.example.serbUber.repository;

import com.example.serbUber.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCity(String city);

    @Query("select l from Location l where l.lon=?1 and l.lat=?2")
    Optional<Location> findByLonAndLat(double lon, double lat);
}
