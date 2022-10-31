package com.example.serbUber.repository;

import com.example.serbUber.model.Verify;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerifyRepository extends MongoRepository<Verify,String> {
    Optional<Verify> findById(String id);
    Optional<Verify> getVerifyById(String id);
}
