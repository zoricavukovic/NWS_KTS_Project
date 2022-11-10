package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegularUserRepository extends JpaRepository<RegularUser, Long> {

    Optional<RegularUser> getRegularUserByEmail(String email);

    Optional<RegularUser> getRegularUserById(Long id);
}
