package com.example.serbUber.repository.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.model.user.DriverUpdateApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverUpdateApprovalRepository extends JpaRepository<DriverUpdateApproval, Long> {

    @Query("select u from DriverUpdateApproval u where u.approved=false")
    List<UserDTO> getAllNotApproved();
}
