package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.role r where u.email=?1")
    Optional<User> getUserByEmail(String email);

    @Query("select u from User u left join fetch u.role r where u.email=?1 and u.verified=true")
    Optional<User> getVerifiedUser(String email);

    @Query("select u from User u left join fetch u.role r where u.role.name='ROLE_ADMIN'")
    List<User> getAdmin();

    @Query("select u from User u left join fetch u.role r where u.role.name='ROLE_ADMIN' and u.online = true")
    List<User> findOnlineAdmin();

    @Query("select u from User u left join fetch u.role r where u.role.name='ROLE_DRIVER' and u.id=?1")
    Optional<Object> getDriverById(long id);

    @Query("select u from User u left join fetch u.role r where u.role.name!='ROLE_ADMIN' and u.verified=true")
    List<User> getAllVerified();
}
