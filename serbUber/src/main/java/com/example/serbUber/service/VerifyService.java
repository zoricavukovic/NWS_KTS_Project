package com.example.serbUber.service;

import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.WrongVerifyTryException;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.VerifyRepository;
import com.example.serbUber.service.interfaces.IVerifyService;
import com.example.serbUber.service.user.UserService;
import com.example.serbUber.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.serbUber.util.Constants.MAX_NUM_VERIFY_TRIES;
import static com.example.serbUber.util.EmailConstants.FRONT_VERIFY_URL;

@Component
@Qualifier("verifyServiceConfiguration")
public class VerifyService implements IVerifyService {

    private final VerifyRepository verifyRepository;

    private final EmailService emailService;

    private final UserService userService;

    public VerifyService(
            final VerifyRepository verifyRepository,
            final EmailService emailService,
            final UserService userService
        ) {
        this.verifyRepository = verifyRepository;
        this.emailService = emailService;
        this.userService = userService;
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
            emailService.sendMail(email, "Verification mail",
                    String.format("Your code is: %d \nClick here to activate your account: %s%s",
                        verifyDTO.getSecurityCode(), FRONT_VERIFY_URL, verifyDTO.getId())
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
          0
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

            throw new WrongVerifyTryException("You tried to verify with wrong code more than 3 times.");
        }
    }

    public void generateNewSecurityCode(final Long verifyId)
            throws EntityNotFoundException, MailCannotBeSentException {
        Verify verify = get(verifyId);
        this.create(verify.getUserId(), verify.getEmail());
        verifyRepository.delete(verify);
    }

    public boolean activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException {
        Verify verify = this.update(verifyId, securityCode);
        User user = userService.getUserById(verify.getUserId());
        user.setVerified(true);
        userService.saveUser(user);

        return true;
    }

    private void saveChanges(final Verify verify, final boolean used) {
        verify.setUsed(used);
        verifyRepository.save(verify);
    }
}
