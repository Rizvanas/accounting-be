package com.rizvanchalilovas.accountingbe.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class ApiException {
    private final HttpStatus status;
    private final List<String> errors;

    @JsonIgnore
    private final ZonedDateTime timestamp;

    public ApiException(HttpStatus status, List<String> errors, ZonedDateTime timestamp) {
        this.status = status;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public ApiException(HttpStatus status, String error, ZonedDateTime timestamp) {
        this.status = status;
        this.errors = Collections.singletonList(error);
        this.timestamp = timestamp;
    }
}
