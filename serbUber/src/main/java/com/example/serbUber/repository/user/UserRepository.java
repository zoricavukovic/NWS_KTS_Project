package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.role r where u.email=?1")
    Optional<User> getUserByEmail(String email);

}
