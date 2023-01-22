package com.example.serbUber.repository;

import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    @Query("select route.locations from Route route where route.id=?1")
    SortedSet<DrivingLocationIndex> getLocationsForRoute(Long id);
}
