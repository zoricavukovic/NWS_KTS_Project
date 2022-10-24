package com.example.serbUber.repository;
import com.example.serbUber.model.Driving;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingRepository extends MongoRepository<Driving, String> {
}
