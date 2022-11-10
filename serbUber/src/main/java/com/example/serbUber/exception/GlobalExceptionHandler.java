package com.example.serbUber.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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
    protected Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errorMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(error ->
            errorMap.put(error.getField(), error.getDefaultMessage())
        );
        return errorMap;
    }
}
