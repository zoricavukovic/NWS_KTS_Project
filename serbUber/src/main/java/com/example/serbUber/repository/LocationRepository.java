package com.example.serbUber.repository;

import com.example.serbUber.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
    Optional<Location> findByCity(String city);
}
