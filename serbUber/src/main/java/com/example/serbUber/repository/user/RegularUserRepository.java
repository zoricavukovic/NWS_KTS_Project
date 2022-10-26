package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.RegularUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RegularUserRepository extends MongoRepository<RegularUser, String>  {

    Optional<RegularUser> getRegularUserByEmail(String email);
}
