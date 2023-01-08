package com.example.serbUber.controller.payment;

import com.example.serbUber.dto.payment.PayingInfoDTO;
import com.example.serbUber.dto.payment.TokenBankDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.PayPalPaymentException;
import com.example.serbUber.model.token.PayingInfo;
import com.example.serbUber.request.payment.CompletePaymentRequest;
import com.example.serbUber.request.payment.UpdatePayingInfoRequest;
import com.example.serbUber.service.payment.PayingInfoService;
import com.example.serbUber.service.payment.TokenBankService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

@RestController
@RequestMapping("/paying-infos")
public class PayingInfoController {

    private final PayingInfoService payingInfoService;

    public PayingInfoController(@Qualifier("payingInfoConfiguration") final PayingInfoService payingInfoService) {
        this.payingInfoService = payingInfoService;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public PayingInfoDTO get(@Valid @NotNull(message = MISSING_ID) @PathVariable Long userId)
            throws EntityNotFoundException
    {

        return payingInfoService.getDefaultPayingInfoDTO();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public PayingInfoDTO updatePayingInfo(@RequestBody @Valid UpdatePayingInfoRequest updatePayingInfoRequest)
            throws EntityNotFoundException
    {

        return payingInfoService.update(
          updatePayingInfoRequest.getTokenPrice(),
          updatePayingInfoRequest.getMaxNumOfTokensPerTransaction()
        );
    }

}
