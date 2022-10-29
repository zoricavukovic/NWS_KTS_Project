package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DriverRepository extends MongoRepository<Driver, String> {

    Optional<Driver> getDriverByEmail(String email);
    Optional<Driver> getDriverById(String id);

    @Query(value = "{ 'id' : ?0 }, {$set: {'verified':true}}")
    Optional<Driver> updateOne(String id);
}
