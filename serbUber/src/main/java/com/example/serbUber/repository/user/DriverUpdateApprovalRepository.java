package com.example.serbUber.repository.user;

import com.example.serbUber.model.user.DriverUpdateApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverUpdateApprovalRepository extends JpaRepository<DriverUpdateApproval, Long> {

}
