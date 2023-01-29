package com.example.serbUber.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Optional;

import static com.example.serbUber.exception.ErrorMessagesConstants.UNAUTHORIZED_MESSAGE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundException(EntityNotFoundException entityNotFoundException) {

        return entityNotFoundException.getMessage();
    }

    @ExceptionHandler(value = DriverAlreadyHasStartedDrivingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String driverAlreadyHasStartedDrivingException(DriverAlreadyHasStartedDrivingException driverAlreadyHasStartedDrivingException) {

        return driverAlreadyHasStartedDrivingException.getMessage();
    }

    @ExceptionHandler(value = DrivingShouldNotStartYetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String drivingShouldNotStartYetException(DrivingShouldNotStartYetException drivingShouldNotStartYetException) {

        return drivingShouldNotStartYetException.getMessage();
    }

    @ExceptionHandler(value = PasswordsDoNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String passwordsDoNotMatchException(PasswordsDoNotMatchException passwordsDoNotMatchException) {

        return passwordsDoNotMatchException.getMessage();
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String entityAlreadyExistsException(EntityAlreadyExistsException entityAlreadyExists) {

        return entityAlreadyExists.getMessage();
    }

    @ExceptionHandler(value = MailCannotBeSentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String mailCannotBeSentException(MailCannotBeSentException mailCannotBeSentException) {

        return mailCannotBeSentException.getMessage();
    }

    @ExceptionHandler(value = WrongVerifyTryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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

    @ExceptionHandler(value = ActivityStatusCannotBeChangedException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String ActivityStatusCannotBeChangedException(ActivityStatusCannotBeChangedException ex) {

        return ex.getMessage();
    }

    @ExceptionHandler(value = PayPalPaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String PayPalPaymentException(PayPalPaymentException ex) {

        return ex.getMessage();
    }

    @ExceptionHandler(value = ExcessiveNumOfPassengersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String excessiveNumOfPassengersException(ExcessiveNumOfPassengersException excessiveNumOfPassengersException) {

        return excessiveNumOfPassengersException.getMessage();
    }

    @ExceptionHandler(value = InvalidChosenTimeForReservationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidChosenTimeForReservationException(InvalidChosenTimeForReservationException invalidChosenTimeForReservationException) {

        return invalidChosenTimeForReservationException.getMessage();
    }

    @ExceptionHandler(value = PassengerNotHaveTokensException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String passengerNotHaveTokensException(PassengerNotHaveTokensException passengerNotHaveTokensException) {

        return passengerNotHaveTokensException.getMessage();
    }

    @ExceptionHandler(value = ReportCannotBeCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String reportCannotBeCreatedException(ReportCannotBeCreatedException reportCannotBeCreatedException) {

        return reportCannotBeCreatedException.getMessage();
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String constraintViolationException(ConstraintViolationException ex) {

        return ex.getMessage();
    }

}
