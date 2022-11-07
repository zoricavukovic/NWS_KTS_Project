package com.example.serbUber.repository;

import com.example.serbUber.model.Verify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyRepository extends JpaRepository<Verify,Long> {

    Optional<Verify> getVerifyById(Long id);

}
