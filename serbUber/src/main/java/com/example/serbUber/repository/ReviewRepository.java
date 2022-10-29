package com.example.serbUber.repository;

import com.example.serbUber.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ReviewRepository extends MongoRepository<Review, String> {

}
