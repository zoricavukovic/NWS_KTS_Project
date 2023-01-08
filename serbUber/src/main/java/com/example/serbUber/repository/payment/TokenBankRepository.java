package com.example.serbUber.repository.payment;

import com.example.serbUber.model.token.TokenBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBankRepository extends JpaRepository<TokenBank, Long> {

    @Query("select tb from TokenBank tb left join fetch tb.transactions t left join fetch tb.user u join fetch tb.payingInfo pi where tb.id=?1")
    Optional<TokenBank> getTokenBankById(Long id);

    @Query("select tb from TokenBank tb left join fetch tb.transactions t left join fetch tb.user u join fetch tb.payingInfo pi where u.id=?1")
    Optional<TokenBank> getTokenBankByUserId(Long id);
}
