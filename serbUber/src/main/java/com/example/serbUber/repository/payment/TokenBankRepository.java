package com.example.serbUber.repository.payment;

import com.example.serbUber.model.token.TokenBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBankRepository extends JpaRepository<TokenBank, Long> {

}
