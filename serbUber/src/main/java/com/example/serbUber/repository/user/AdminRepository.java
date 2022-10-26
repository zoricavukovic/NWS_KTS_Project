package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {

    Optional<Admin> getAdminByEmail(String email);
}
