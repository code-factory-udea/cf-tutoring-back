package co.udea.codefact.utils.controller;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalHandlerAdvice {

    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDataAlreadyExistsException(DataAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleDataNotFoundException(DataNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(InvalidRoleChangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidRoleChangeException(InvalidRoleChangeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(InvalidBodyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleInvalidBodyException(InvalidBodyException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error ->
            errorMessage.append(error.getDefaultMessage()).append(". ")
        );
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, errorMessage.toString());
        return errorResponse;
    }

    @ExceptionHandler(TutorErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleScheduleNoCreatedException(TutorErrorException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDateTimeParseException() {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, MessagesConstants.ERROR_PARSING_HOUR_DAY);
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidEnumException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MessagesConstants.ERROR_EXCEPTION_MESSAGE_BODY, MessagesConstants.ERROR_PARSING_HOUR_DAY);
        return errorResponse;
    }

}
