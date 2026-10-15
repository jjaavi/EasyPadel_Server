package com.easypadel.controller;

import com.easypadel.dto.PistaRequest;
import com.easypadel.dto.PistaResponse;
import com.easypadel.service.PistaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pistes")
public class PistaController {

    private final PistaService pistaService;

    public PistaController(PistaService pistaService) {
        this.pistaService = pistaService;
    }

    //POST /api/pistes
    @PostMapping
    public ResponseEntity<PistaResponse> create(@Valid @RequestBody PistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pistaService.create(request));
    }

    //PUT /api/pistes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PistaResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody PistaRequest request) {
        return ResponseEntity.ok(pistaService.update(id, request));
    }

    //DELETE /api/pistes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pistaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //GET /api/pistes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PistaResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pistaService.getById(id));
    }

    //GET /api/pistes
    @GetMapping
    public ResponseEntity<List<PistaResponse>> listAllPistes() {
        return ResponseEntity.ok(pistaService.listAllPistes());
    }

    //GET /api/pistes/actives
    @GetMapping("/actives")
    public ResponseEntity<List<PistaResponse>> llistarActives() {
        return ResponseEntity.ok(pistaService.llistarActives());
    }
}
