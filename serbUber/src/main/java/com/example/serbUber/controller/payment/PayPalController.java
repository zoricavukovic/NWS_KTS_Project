package com.example.serbUber.controller.payment;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.PayPalPaymentException;
import com.example.serbUber.request.payment.CompletePaymentRequest;
import com.example.serbUber.request.payment.CreatePaymentRequest;
import com.example.serbUber.service.payment.PayPalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/paypal")
public class PayPalController {

    private final PayPalService payPalService;

    public PayPalController(@Qualifier("paypalServiceConfiguration") final PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @PostMapping("/create-payment")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public Map<String, String> createPayment(@Valid @RequestBody CreatePaymentRequest createPaymentRequest)
            throws EntityNotFoundException, PayPalPaymentException {

        return payPalService.createPayment(
                createPaymentRequest.getTokenBankId(),
                createPaymentRequest.getNumOfTokens()
        );
    }

    @PostMapping("/complete-payment")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public boolean completePayment(@RequestBody CompletePaymentRequest completePaymentRequest)
            throws PayPalPaymentException, EntityNotFoundException {

        return payPalService.completePayment(
                completePaymentRequest.getPaymentId(),
                completePaymentRequest.getPayerId(),
                completePaymentRequest.getNumOfTokens(),
                completePaymentRequest.getTokenBankId()
        );
    }
}
