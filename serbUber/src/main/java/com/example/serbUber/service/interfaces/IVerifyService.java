package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.MailCannotBeSentException;
import com.example.serbUber.exception.WrongVerifyTryException;
import com.example.serbUber.model.Verify;
import org.springframework.stereotype.Service;

import static com.example.serbUber.util.EmailConstants.FRONT_VERIFY_URL;

@Service
public interface IVerifyService {
    Verify get(final Long id) throws EntityNotFoundException;

    VerifyDTO create(
            final Long userId,
            final String email
    ) throws MailCannotBeSentException;

    Verify update(final Long id, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException;

    void generateNewSecurityCode(final Long verifyId)
            throws EntityNotFoundException, MailCannotBeSentException;
}
