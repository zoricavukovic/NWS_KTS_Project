package com.example.serbUber.service.payment;

import com.example.serbUber.model.token.TokenTransaction;
import com.example.serbUber.repository.payment.TokenTransactionRepository;
import com.example.serbUber.service.interfaces.ITokenTransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Qualifier("tokenTransactionConfiguration")
public class TokenTransactionService implements ITokenTransactionService {

    private final TokenTransactionRepository tokenTransactionRepository;

    public TokenTransactionService(final TokenTransactionRepository tokenTransactionRepository) {
        this.tokenTransactionRepository = tokenTransactionRepository;
    }

    public TokenTransaction createTransactionObject(final double numOfTokens, final double pricePerToken) {

        return new TokenTransaction(LocalDateTime.now(), numOfTokens, numOfTokens * pricePerToken);
    }

}
