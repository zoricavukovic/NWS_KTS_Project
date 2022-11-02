package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.LoginUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginUserInfoRepository extends JpaRepository<LoginUserInfo, Long> {

    Optional<LoginUserInfo> findByEmail(String email);
}
