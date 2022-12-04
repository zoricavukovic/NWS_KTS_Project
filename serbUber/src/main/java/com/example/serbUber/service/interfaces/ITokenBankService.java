package com.example.serbUber.service.interfaces;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.model.user.RegularUser;

public interface ITokenBankService {

    TokenBank getTokenBankById(final Long id) throws EntityNotFoundException;
    TokenBank updateTokenBank(
            final Long tokenBankId,
            final int numOfTokens
    ) throws EntityNotFoundException;

    TokenBank createTokenBank(RegularUser regularUser) throws EntityNotFoundException;
}
