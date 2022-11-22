package com.example.serbUber.repository.user;

import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegularUserRepository extends JpaRepository<RegularUser, Long> {

    Optional<RegularUser> getRegularUserByEmail(String email);

    @Query("select u from RegularUser u left join fetch u.favouriteRoutes route where u.id = ?2 and route.id = ?1")
    RegularUser getUserWithFavouriteRouteId(Long routeId, Long userId);
    Optional<RegularUser> getRegularUserById(Long id);

}
