package com.example.serbUber.service;

import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.WrongVerifyTryException;
import com.example.serbUber.model.Verify;
import com.example.serbUber.repository.VerifyRepository;
import com.example.serbUber.service.interfaces.IVerifyService;
import com.example.serbUber.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.serbUber.util.Constants.MAX_NUM_VERIFY_TRIES;
import static com.example.serbUber.util.EmailConstants.FRONT_VERIFY_URL;

@Component
@Qualifier("verifyServiceConfiguration")
public class VerifyService implements IVerifyService {

    private final VerifyRepository verifyRepository;

    private final EmailService emailService;


    public VerifyService(
            final VerifyRepository verifyRepository,
            final EmailService emailService
        ) {
        this.verifyRepository = verifyRepository;
        this.emailService = emailService;
    }

    public Verify get(Long id) throws EntityNotFoundException {

        return verifyRepository.getVerifyById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.VERIFY));
    }

    public VerifyDTO create(
            final Long userId,
            final String email
    ) throws MailCannotBeSentException {
        try {
            VerifyDTO verifyDTO = this.save(userId, email);
            emailService.sendVerificationMail(
                verifyDTO.getSecurityCode(),
                String.format("%s%s",FRONT_VERIFY_URL, verifyDTO.getId())
            );

            return verifyDTO;

        } catch (Exception e) {
            throw new MailCannotBeSentException(email);
        }
    }

    private VerifyDTO save(final Long userId, final String email) {

        return new VerifyDTO(verifyRepository.save(new Verify(
          userId,
          email,
          Constants.generateSecurityCode(),
          false,
          0,
          getExpirationTime()
        )));
    }

    public Verify update(final Long id, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException
    {
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

            throw new WrongVerifyTryException("Your verification code is either expired or typed wrong 3 times. Reset code.");
        }
    }

    public void generateNewSecurityCode(final Long verifyId)
            throws EntityNotFoundException, MailCannotBeSentException {
        Verify verify = get(verifyId);
        create(verify.getUserId(), verify.getEmail());
        verifyRepository.delete(verify);
    }

    private LocalDateTime getExpirationTime() {

        return LocalDateTime.now().plusMinutes(10);
    }

    private void saveChanges(final Verify verify, final boolean used) {
        verify.setUsed(used);
        verifyRepository.save(verify);
    }
}
