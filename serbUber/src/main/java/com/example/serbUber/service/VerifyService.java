package com.example.serbUber.service;

import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.WrongVerifyTryException;
import com.example.serbUber.model.Verify;
import com.example.serbUber.repository.VerifyRepository;
import com.example.serbUber.util.Constants;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerifyService {

    private final VerifyRepository verifyRepository;

    private final EmailService emailService;

    public VerifyService(
            final VerifyRepository verifyRepository,
            final EmailService emailService
        ) {
        this.verifyRepository = verifyRepository;
        this.emailService = emailService;
    }

    public Verify get(String id) throws EntityNotFoundException {
        Optional<Verify> optionalVerify = verifyRepository.getVerifyById(id);

        if (optionalVerify.isPresent()){

            return optionalVerify.get();
        }

        throw new EntityNotFoundException(id, EntityType.VERIFY);
    }

    public String sendEmail(String userId, String email) throws MailCannotBeSentException {
        try {
            VerifyDTO verify = this.create(userId, email);
            emailService.sendMail(email, "Verification mail",
                    "Your code is: " + verify.getSecurityCode() + "\nClick here to activate your account: http://localhost:4200/verify/" + verify.getId()
            );

            return verify.getId();
        } catch (Exception e) {
            throw new MailCannotBeSentException("Something went wrong. Email to " + email + " cannot be sent.");
        }
    }

    public VerifyDTO create(
            final String userId,
            final String email
    ) {

        return new VerifyDTO(verifyRepository.save(new Verify(
          userId,
          email,
          Constants.generateSecurityCode(),
          false,
          0
        )));
    }

    public void update(String id, int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException {
        Verify verify = get(id);

        if (verify.isNotUsed() && verify.hasTries() && verify.checkSecurityCode(securityCode)){
            verify.setNumOfTries(verify.getNumOfTries() + 1);
            verify.setUsed(true);
            verifyRepository.save(verify);

        } else if (verify.hasTries()){
            verify.setNumOfTries(verify.getNumOfTries() + 1);
            verify.setUsed(verify.getNumOfTries() >= 3);
            verifyRepository.save(verify);

            throw new WrongVerifyTryException("Your security code is not accepted. Try again.");
        } else {
            verify.setUsed(true);
            verifyRepository.save(verify);

            throw new WrongVerifyTryException("You tried to verify with wrong code more than 3 times.");
        }
    }

}
