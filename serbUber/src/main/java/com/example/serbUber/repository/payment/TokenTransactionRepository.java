package com.example.serbUber.repository.payment;


import com.example.serbUber.model.token.TokenTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenTransactionRepository  extends JpaRepository<TokenTransaction, Long> {
}
