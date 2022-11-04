package com.example.serbUber.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PasswordsDoNotMatchException.class)
    public ResponseEntity<String> passwordsDoNotMatchException(PasswordsDoNotMatchException passwordsDoNotMatchException) {
        return new ResponseEntity<>(passwordsDoNotMatchException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    public ResponseEntity<String> entityAlreadyExistsException(EntityAlreadyExistsException entityAlreadyExists) {
        return new ResponseEntity<>(entityAlreadyExists.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MailCannotBeSentException.class)
    public ResponseEntity<String> mailCannotBeSentException(MailCannotBeSentException mailCannotBeSentException) {
        return new ResponseEntity<>(mailCannotBeSentException.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = WrongVerifyTryException.class)
    public ResponseEntity<String> wrongVerifyTryException(WrongVerifyTryException wrongVerifyTryException) {
        return new ResponseEntity<>(wrongVerifyTryException.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = UsersUpdateException.class)
    public ResponseEntity<String> usersUpdateException(UsersUpdateException usersUpdateException) {
        return new ResponseEntity<>(usersUpdateException.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
}
