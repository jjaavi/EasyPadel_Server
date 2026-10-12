package com.easypadel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BackendException.class)
    public ResponseEntity<Map<String, Object>> handleBackend(BackendException e) {
        return resposta(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception e) {
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Error intern del servidor.");
    }

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String missatge) {
        return ResponseEntity.status(status)
                .body(Map.of("status", status.value(), "missatge", missatge));
    }
}
