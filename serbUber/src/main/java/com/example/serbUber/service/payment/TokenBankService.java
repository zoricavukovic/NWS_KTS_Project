package com.example.serbUber.service.payment;

import com.example.serbUber.dto.payment.TokenBankDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.model.token.TokenTransaction;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.payment.TokenBankRepository;
import com.example.serbUber.service.interfaces.ITokenBankService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import static com.example.serbUber.util.Constants.EMPTY_BANK_ACCOUNT;
import static com.example.serbUber.util.Constants.ZERO_TOKENS;

@Component
@Qualifier("tokenBankConfiguration")
public class TokenBankService implements ITokenBankService {

    private final TokenBankRepository tokenBankRepository;

    private final TokenTransactionService tokenTransactionService;

    private final PayingInfoService payingInfoService;

    public TokenBankService(
            final TokenBankRepository tokenBankRepository,
            final TokenTransactionService tokenTransactionService,
            final PayingInfoService payingInfoService
    ) {
        this.tokenBankRepository = tokenBankRepository;
        this.tokenTransactionService = tokenTransactionService;
        this.payingInfoService = payingInfoService;
    }

    public TokenBank getTokenBankById(final Long id) throws EntityNotFoundException {

        return tokenBankRepository.getTokenBankById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.TOKEN_BANK));
    }

    public TokenBank getTokenBankByUserId(final Long userId) throws EntityNotFoundException {

        return tokenBankRepository.getTokenBankByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, EntityType.TOKEN_BANK));
    }

    public TokenBank updateTokenBank(
            final Long tokenBankId,
            final double numOfTokens
    ) throws EntityNotFoundException {
        TokenBank tokenBank = getTokenBankById(tokenBankId);
        TokenTransaction tokenTransaction = this.tokenTransactionService.createTransactionObject(
                numOfTokens, tokenBank.getPayingInfo().getTokenPrice());
        tokenBank.addTokens(numOfTokens);
        tokenBank.addTotalSpent(tokenTransaction.getTotalPrice());
        tokenBank.getTransactions().add(tokenTransaction);

        return tokenBankRepository.save(tokenBank);
    }

    public TokenBankDTO createTokenBank(final RegularUser regularUser) throws EntityNotFoundException {

        return new TokenBankDTO(
                tokenBankRepository.save(new TokenBank(
                regularUser,
                ZERO_TOKENS,
                EMPTY_BANK_ACCOUNT,
                EMPTY_BANK_ACCOUNT,
                payingInfoService.getDefaultPayingInfo()))
        );
    }

    public TokenBankDTO getByUserId(Long userId) throws EntityNotFoundException {

        return new TokenBankDTO(getTokenBankByUserId(userId));
    }
}
