package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.LoginUserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LoginUserInfoRepository extends MongoRepository<LoginUserInfo, String> {

    Optional<LoginUserInfo> findByEmail(String email);
}
