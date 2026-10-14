package com.easypadel.controller;

import com.easypadel.dto.CanviarPasswordRequest;
import com.easypadel.dto.LoginRequest;
import com.easypadel.dto.LoginResponse;
import com.easypadel.dto.UsuariCreateRequest;
import com.easypadel.dto.UsuariResponse;
import com.easypadel.dto.UsuariUpdateRequest;
import com.easypadel.service.UsuariService;
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
@RequestMapping("/api/usuaris")
public class UsuariController {

    private final UsuariService usuariService;

    public UsuariController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    //POST /api/usuaris
    @PostMapping
    public ResponseEntity<UsuariResponse> create(@Valid @RequestBody UsuariCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariService.create(request));
    }

    //PUT /api/usuaris/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UsuariResponse> update(@PathVariable Integer id,
                                                 @Valid @RequestBody UsuariUpdateRequest request) {
        return ResponseEntity.ok(usuariService.update(id, request));
    }

    //DELETE /api/usuaris/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuariService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //GET /api/usuaris/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UsuariResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuariService.getById(id));
    }

    //GET /api/usuaris
    @GetMapping
    public ResponseEntity<List<UsuariResponse>> listAllUsers() {
        return ResponseEntity.ok(usuariService.listAllUsers());
    }

    //POST /api/usuaris/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuariService.login(request));
    }

    //PUT /api/usuaris/{id}/password
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id,
                                               @Valid @RequestBody CanviarPasswordRequest request) {
        usuariService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }
}
