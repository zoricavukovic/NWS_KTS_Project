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

import static com.example.serbUber.util.Constants.FRONT_VERIFY_URL;
import static com.example.serbUber.util.Constants.MAX_NUM_VERIFY_TRIES;

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

    public void sendEmail(
            final String userId,
            final String email
    ) throws MailCannotBeSentException {
        try {
            VerifyDTO verify = this.create(userId, email);
            emailService.sendMail(email, "Verification mail",
                    String.format("Your code is: %d \nClick here to activate your account: %s%s",
                            verify.getSecurityCode(), FRONT_VERIFY_URL, verify.getId())
            );

        } catch (Exception e) {
            throw new MailCannotBeSentException(String.format("Something went wrong. Email to %s" +
                    " cannot be sent.", email));
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

    public Verify update(final String id, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException {

        Verify verify = get(id);
        if (verify.canVerify(securityCode)) {
            verify.incrementNumOfTries();
            this.saveChanges(verify, true);

            return verify;
        } else if (verify.wrongCodeButHasTries()){
            this.saveChanges(verify, verify.incrementNumOfTries() >= MAX_NUM_VERIFY_TRIES);

            throw new WrongVerifyTryException("Your security code is not accepted. Try again.");
        } else {
            saveChanges(verify, true);

            throw new WrongVerifyTryException("You tried to verify with wrong code more than 3 times.");
        }
    }

    private void saveChanges(final Verify verify, final boolean used) {
        verify.setUsed(used);
        verifyRepository.save(verify);
    }

    public void generateNewSecurityCode(final String verifyId)
            throws EntityNotFoundException, MailCannotBeSentException {
        Verify verify = get(verifyId);
        this.sendEmail(verify.getUserId(), verify.getEmail());
        verifyRepository.delete(verify);
    }
}
