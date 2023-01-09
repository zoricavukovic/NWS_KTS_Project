package com.example.serbUber.controller.payment;

import com.example.serbUber.dto.payment.TokenBankDTO;
import com.example.serbUber.dto.payment.TotalInAppSpendingDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.service.payment.PayPalService;
import com.example.serbUber.service.payment.TokenBankService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

@RestController
@RequestMapping("/token-banks")
public class TokenBankController {

    private final TokenBankService tokenBankService;

    public TokenBankController(@Qualifier("tokenBankConfiguration") final TokenBankService tokenBankService) {
        this.tokenBankService = tokenBankService;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public TokenBankDTO get(@Valid @NotNull(message = MISSING_ID) @PathVariable Long userId)
            throws EntityNotFoundException
    {

        return tokenBankService.getByUserId(userId);
    }

    @GetMapping("/in-app-spending")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public TotalInAppSpendingDTO getInAppSpending()
    {

        return tokenBankService.getInAppSpending();
    }

}
