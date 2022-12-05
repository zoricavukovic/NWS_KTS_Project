package com.example.serbUber.service.payment;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.PayPalPaymentException;
import com.example.serbUber.model.token.TokenBank;
import com.example.serbUber.service.interfaces.IPayPalService;
import com.paypal.api.payments.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import static com.example.serbUber.util.Constants.*;

@Component
@Qualifier("paypalServiceConfiguration")
public class PayPalService implements IPayPalService {

    private final TokenBankService tokenBankService;

    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    public PayPalService(final TokenBankService tokenBankService) {this.tokenBankService = tokenBankService;}

    public Map<String, String> createPayment(
            Long tokenBankId,
            int numOfTokens
    ) throws EntityNotFoundException, PayPalPaymentException {
        TokenBank tokenBank = this.tokenBankService.getTokenBankById(tokenBankId);
        checkIfNumOfTokensAllowed(tokenBank.getPayingInfo().getMaxNumOfTokensPerTransaction(), numOfTokens);

        Amount amount = createAmount(tokenBank, numOfTokens);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction(amount));

        Payer payer = createPayer();
        Payment payment = createPayment(payer, transactions);
        payment.setRedirectUrls(createRedirectUrls());

        return createResponse(payment);
    }

    public boolean completePayment(
            final String paymentId,
            final String payerId,
            final int numOfTokens,
            final Long tokenBankId
    )
            throws PayPalPaymentException, EntityNotFoundException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return createCompletePaymentResponse(payment, paymentExecution, numOfTokens, tokenBankId);
    }

    private Amount createAmount(final TokenBank tokenBank, final int numOfTokens) {
        Amount amount = new Amount();
        amount.setCurrency(tokenBank.getPayingInfo().getCurrency());
        amount.setTotal(String.format("%.2f", getTotalPrice(numOfTokens, tokenBank.getPayingInfo().getTokenPrice())));

        return amount;
    }

    private Transaction createTransaction(final Amount amount) {
        Transaction transaction = new Transaction();
        transaction.setDescription(TRANSACTION_DESCRIPTION);
        transaction.setAmount(amount);

        return transaction;
    }

    private Payer createPayer() {
        Payer payer = new Payer();
        payer.setPaymentMethod(PAYMENT_METHOD);

        return payer;
    }

    private Payment createPayment(final Payer payer, final List<Transaction> transactions) {
        Payment payment = new Payment();
        payment.setIntent(PAYMENT_INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        return  payment;
    }

    private RedirectUrls createRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(REDIRECT_URL_SUCCESS);
        redirectUrls.setReturnUrl(REDIRECT_URL_CANCEL);

        return redirectUrls;
    }

    private Map<String, String> createResponse(final Payment payment)
            throws PayPalPaymentException
    {
        Map<String, String> response = new HashMap<String, String>();
        try {
            response = populateResponseWithLink(payment, response);
        } catch (PayPalRESTException e) {
            throw new PayPalPaymentException();
        }

        return response;
    }

    private Map<String, String> populateResponseWithLink(
            final Payment payment,
            final Map<String, String> response
    ) throws PayPalRESTException {
        Payment createdPayment;
        String redirectUrl = "";
        APIContext context = new APIContext(clientId, clientSecret, mode);
        createdPayment = payment.create(context);
        if (createdPayment!=null){
            List<Links> links = createdPayment.getLinks();
            for (Links link:links) {
                if(link.getRel().equals(PAYPAL_APPROVAL_URL)){
                    redirectUrl = link.getHref();
                    break;
                }
            }
            response.put("status", "success");
            response.put("redirectUrl", redirectUrl);
        }

        return response;
    }

    private void checkIfNumOfTokensAllowed(int maxNumOfTokens, int wantedNumOfTokens)
            throws PayPalPaymentException
    {
        if (wantedNumOfTokens > maxNumOfTokens) {
            throw new PayPalPaymentException(String.format("You cannot but more than %s per tokens transaction.",
                    maxNumOfTokens));
        }
    }

    private double getTotalPrice(int numOfTokens, double pricePerToken) {

        return numOfTokens * pricePerToken;
    }

    private boolean createCompletePaymentResponse(
            final Payment payment,
            final PaymentExecution paymentExecution,
            final int numOfTokens,
            final Long tokenBankId
    ) throws PayPalPaymentException, EntityNotFoundException {
        try {
            APIContext context = new APIContext(clientId, clientSecret, mode);
            Payment createdPayment = payment.execute(context, paymentExecution);
            if (createdPayment != null) {
                this.tokenBankService.updateTokenBank(tokenBankId, numOfTokens);

                return true;
            }
        } catch (PayPalRESTException e) {
            throw new PayPalPaymentException("Payment cannot be performed. Check your balance on paypal account.");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(tokenBankId, EntityType.TOKEN_BANK);
        }

        return false;
    }

}
