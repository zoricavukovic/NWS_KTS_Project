package com.example.serbUber.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.serbUber.exception.ErrorMessagesConstants.UNAUTHORIZED_MESSAGE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundException(EntityNotFoundException entityNotFoundException) {

        return entityNotFoundException.getMessage();
    }

    @ExceptionHandler(value = PasswordsDoNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String passwordsDoNotMatchException(PasswordsDoNotMatchException passwordsDoNotMatchException) {

        return passwordsDoNotMatchException.getMessage();
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String entityAlreadyExistsException(EntityAlreadyExistsException entityAlreadyExists) {

        return entityAlreadyExists.getMessage();
    }

    @ExceptionHandler(value = MailCannotBeSentException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String mailCannotBeSentException(MailCannotBeSentException mailCannotBeSentException) {

        return mailCannotBeSentException.getMessage();
    }

    @ExceptionHandler(value = WrongVerifyTryException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String wrongVerifyTryException(WrongVerifyTryException wrongVerifyTryException) {

        return wrongVerifyTryException.getMessage();
    }

    @ExceptionHandler(value = EntityUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String entityUpdateException(EntityUpdateException usersUpdateException) {

        return usersUpdateException.getMessage();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        Optional<FieldError> error = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream().findFirst();

        return error.map(
            fieldError -> String.format("%s", fieldError.getDefaultMessage()))
            .orElse("Error not found");
    }

    @ExceptionHandler(value = NoAvailableAdminException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String NoAvailableAdminException(NoAvailableAdminException noAvailableAdminException) {

        return noAvailableAdminException.getMessage();
    }

    @ExceptionHandler({ AuthenticationException.class, AccessDeniedException.class })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public final String handleAccessDeniedException(Exception ex) {

        return UNAUTHORIZED_MESSAGE;
    }
}
