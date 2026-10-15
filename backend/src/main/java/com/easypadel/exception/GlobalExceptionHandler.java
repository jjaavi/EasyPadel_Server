package com.easypadel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BackendException.class)
    public ResponseEntity<Map<String, Object>> handleBackend(BackendException e) {
        return resposta(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        return resposta(HttpStatus.BAD_REQUEST, "Dades de la petició no vàlides.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException e) {
        return resposta(HttpStatus.BAD_REQUEST, "El cos de la petició no és vàlid.");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleBadParameter(Exception e) {
        return resposta(HttpStatus.BAD_REQUEST, "Els paràmetres de la petició no són vàlids.");
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
