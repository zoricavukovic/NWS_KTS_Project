package com.example.serbUber.repository;

import com.example.serbUber.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.receiver.id = ?1 order by r.timeStamp desc")
    List<Report> getAllForUser(Long id);
}
