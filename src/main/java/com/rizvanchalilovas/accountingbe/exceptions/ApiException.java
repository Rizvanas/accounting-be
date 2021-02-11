package com.rizvanchalilovas.accountingbe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class ApiException {
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;
    private final ZonedDateTime timestamp;

    public ApiException(HttpStatus status, String message, List<String> errors, ZonedDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public ApiException(HttpStatus status, String message, String error, ZonedDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
        this.timestamp = timestamp;
    }
}
