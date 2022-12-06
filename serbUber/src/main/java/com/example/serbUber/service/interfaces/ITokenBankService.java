package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.payment.TokenBankDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.model.user.RegularUser;

public interface ITokenBankService {

    TokenBank getTokenBankById(final Long id) throws EntityNotFoundException;
    TokenBank updateTokenBank(
            final Long tokenBankId,
            final double numOfTokens
    ) throws EntityNotFoundException;

    TokenBankDTO createTokenBank(RegularUser regularUser) throws EntityNotFoundException;

    TokenBankDTO getByUserId(Long userId) throws EntityNotFoundException;
}
