package com.example.serbUber.repository;

import com.example.serbUber.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
