package com.easypadel.exception;

import org.springframework.http.HttpStatus;

public class BackendException extends RuntimeException {

    private final HttpStatus status;

    public BackendException(HttpStatus status, String missatge) {
        super(missatge);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
