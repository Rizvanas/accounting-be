package com.rizvanchalilovas.accountingbe.exceptions.handlers;

import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.exceptions.ApiException;
import com.rizvanchalilovas.accountingbe.exceptions.ApiRequestException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {
            ApiRequestException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            MissingPathVariableException.class
    })
    public ResponseEntity<Object> handleApiRequestException(RuntimeException e) {
        var exception = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        var exception = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> handleIllegalStateException(Exception e) {
        var exception = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }
}
