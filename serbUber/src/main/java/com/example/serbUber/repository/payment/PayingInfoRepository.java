package com.example.serbUber.repository.payment;

import com.example.serbUber.model.token.PayingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayingInfoRepository extends JpaRepository<PayingInfo, Long> {
}
