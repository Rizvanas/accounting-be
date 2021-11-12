package com.rizvanchalilovas.accountingbe.exceptions.handlers;

import com.rizvanchalilovas.accountingbe.exceptions.AlreadyExistsException;
import com.rizvanchalilovas.accountingbe.exceptions.ApiException;
import com.rizvanchalilovas.accountingbe.exceptions.ApiRequestException;
import com.rizvanchalilovas.accountingbe.security.JwtAuthenticationException;
import javassist.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getClass().getSimpleName() + " " + error.getObjectName() + ": "
                    + error.getDefaultMessage());
        }

        ApiException apiError = new ApiException(
                HttpStatus.BAD_REQUEST,
                errors,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        String error = ex.getParameterName() + " parameter is missing";

        ApiException apiException = new ApiException(
                HttpStatus.BAD_REQUEST,
                error,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(
                apiException, new HttpHeaders(), apiException.getStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ApiException apiError = new ApiException(
                HttpStatus.NOT_FOUND,
                error,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        ApiException apiError = new ApiException(
                HttpStatus.METHOD_NOT_ALLOWED,
                builder.toString(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getRootBeanClass().getSimpleName() + " " +
                        v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());

        var apiError = new ApiException(
                HttpStatus.BAD_REQUEST,
                errors,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String error =
                ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();

        ApiException apiError = new ApiException(
                HttpStatus.BAD_REQUEST,
                error,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        ApiException apiError = new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "error occurred",
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = {
            ApiRequestException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<Object> handleApiRequestException(RuntimeException ex) {
        var exception = new ApiException(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        var exception = new ApiException(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> handleIllegalStateException(Exception ex) {
        var exception = new ApiException(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }

    @ExceptionHandler(value = {JwtAuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(JwtAuthenticationException ex) {
        var exception = new ApiException(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, exception.getStatus());
    }
}
