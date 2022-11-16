package com.example.serbUber.repository;

import com.example.serbUber.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r left join fetch r.driving d where d.driverId = ?1")
    List<Review> findAllByDriverId(Long id);

    @Query("select r from Review r left join fetch r.driving d where d.id=?1")
    Review findByDrivingId(Long id);

    @Query("select r from Review r left join fetch r.driving d left join fetch r.sender u where u.id=?1")
    List<Review> findAllReviewedDrivingIdForUser(Long id);
}
