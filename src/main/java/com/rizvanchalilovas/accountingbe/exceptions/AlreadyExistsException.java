package com.rizvanchalilovas.accountingbe.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
