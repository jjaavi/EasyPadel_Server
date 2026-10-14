package com.easypadel.controller;

import com.easypadel.dto.DisponibilitatPistaResponse;
import com.easypadel.dto.ReservaRequest;
import com.easypadel.dto.ReservaResponse;
import com.easypadel.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reserves")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    //POST /api/reserves
    @PostMapping
    public ResponseEntity<ReservaResponse> create(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.create(request));
    }

    //PUT /api/reserves/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> update(@PathVariable Integer id,
                                                  @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.update(id, request));
    }

    //DELETE /api/reserves/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //GET /api/reserves/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.getById(id));
    }

    //GET /api/reserves
    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listAllReservas() {
        return ResponseEntity.ok(reservaService.listAllReservas());
    }

    //GET /api/reserves/usuari/{usuariId}
    @GetMapping("/usuari/{usuariId}")
    public ResponseEntity<List<ReservaResponse>> listReservesByUsuari(@PathVariable Integer usuariId) {
        return ResponseEntity.ok(reservaService.listReservesByUsuari(usuariId));
    }

    //GET /api/reserves/usuari/{usuariId}/posteriors
    @GetMapping("/usuari/{usuariId}/posteriors")
    public ResponseEntity<List<ReservaResponse>> listReservesPosteriorsPerUsuariId(@PathVariable Integer usuariId) {
        return ResponseEntity.ok(reservaService.listReservesPosteriorsPerUsuariId(usuariId));
    }

    //GET /api/reserves/usuari/{usuariId}/anteriors?data=2026-10-14
    @GetMapping("/usuari/{usuariId}/anteriors")
    public ResponseEntity<List<ReservaResponse>> listReservesAnteriorsPerUsuariId(
            @PathVariable Integer usuariId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(reservaService.listReservesAnteriorsPerUsuariId(usuariId, data));
    }

    //GET /api/reserves/data/2026-10-14
    @GetMapping("/data/{data}")
    public ResponseEntity<List<ReservaResponse>> listReservesByData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(reservaService.listReservesByData(data));
    }

    //GET /api/reserves/pista/{pistaId}?data=2026-10-14
    @GetMapping("/pista/{pistaId}")
    public ResponseEntity<List<ReservaResponse>> listReservesByPistaAndData(
            @PathVariable Integer pistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(reservaService.listReservesByPistaAndData(pistaId, data));
    }

    //GET /api/reserves/cercar?data=2026-10-14&pistaId=1&usuariId=1  (tots els parametres son opcionals)
    @GetMapping("/cercar")
    public ResponseEntity<List<ReservaResponse>> searchForAdmin(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Integer pistaId,
            @RequestParam(required = false) Integer usuariId) {
        return ResponseEntity.ok(reservaService.searchForAdmin(data, pistaId, usuariId));
    }

    //GET /api/reserves/disponibilitat/{pistaId}?data=2026-10-14
    @GetMapping("/disponibilitat/{pistaId}")
    public ResponseEntity<DisponibilitatPistaResponse> getDisponibilitat(
            @PathVariable Integer pistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(reservaService.getDisponibilitat(pistaId, data));
    }
}
