package com.easypadel.service;

import com.easypadel.dto.PistaRequest;
import com.easypadel.dto.PistaResponse;
import com.easypadel.entity.Pista;
import com.easypadel.exception.BackendException;
import com.easypadel.repository.PistaRepository;
import com.easypadel.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PistaService {

    private final PistaRepository pistaRepository;
    private final ReservaRepository reservaRepository;

    public PistaService(PistaRepository pistaRepository, ReservaRepository reservaRepository) {
        this.pistaRepository = pistaRepository;
        this.reservaRepository = reservaRepository;
    }

    public PistaResponse create(PistaRequest request) {
        String nom = request.nom().trim();
        if (pistaRepository.existsByNomIgnoreCase(nom)) {
            throw new BackendException(HttpStatus.CONFLICT, "Ja existeix una pista amb el nom " + nom);
        }

        Pista pista = new Pista();
        pista.setNom(nom);
        pista.setActiva(request.activa() == null || request.activa());

        return PistaResponse.fromEntity(pistaRepository.save(pista));
    }

    public PistaResponse update(Integer id, PistaRequest request) {
        Pista pista = findById(id);
        String nom = request.nom().trim();

        if (pistaRepository.existsByNomIgnoreCaseAndIdNot(nom, id)) {
            throw new BackendException(HttpStatus.CONFLICT, "Ja existeix una altra pista amb el nom " + nom);
        }

        pista.setNom(nom);
        if (request.activa() != null) {
            pista.setActiva(request.activa());
        }

        return PistaResponse.fromEntity(pistaRepository.save(pista));
    }

    public void delete(Integer id) {
        Pista pista = findById(id);

        if (reservaRepository.existsByPistaId(id)) {
            throw new BackendException(HttpStatus.CONFLICT,
                    "No es pot eliminar la pista perquè té reserves associades");
        }

        pistaRepository.delete(pista);
    }

    private Pista findById(Integer id) {
        return pistaRepository.findById(id)
                .orElseThrow(() -> new BackendException(HttpStatus.NOT_FOUND, "Pista no trobada amb id: " + id));
    }

    @Transactional(readOnly = true)
    public PistaResponse getById(Integer id) {
        return PistaResponse.fromEntity(findById(id));
    }

    @Transactional(readOnly = true)
    public List<PistaResponse> listAllPistes() {
        return pistaRepository.findAll()
                .stream()
                .map(PistaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PistaResponse> llistarActives() {
        return pistaRepository.findByActivaTrueOrderByIdAsc()
                .stream()
                .map(PistaResponse::fromEntity)
                .toList();
    }
}
