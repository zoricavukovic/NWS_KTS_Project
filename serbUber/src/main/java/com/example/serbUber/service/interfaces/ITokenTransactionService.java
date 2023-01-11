package com.example.serbUber.service.interfaces;

import com.example.serbUber.model.token.TokenTransaction;

public interface ITokenTransactionService {

    TokenTransaction createTransactionObject(final double numOfTokens, final double pricePerToken);
}
