package com.easypadel.controller;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConnectionController {

    private final ApplicationAvailability availability;

    public ConnectionController(ApplicationAvailability availability) {
        this.availability = availability;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        boolean ready = availability.getReadinessState() == ReadinessState.ACCEPTING_TRAFFIC;
        return ResponseEntity.status(ready ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("status", ready ? "UP" : "DOWN"));
    }
}
