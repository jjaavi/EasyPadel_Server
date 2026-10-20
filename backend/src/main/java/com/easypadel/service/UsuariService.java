package com.easypadel.service;

import com.easypadel.dto.CanviarPasswordRequest;
import com.easypadel.dto.LoginRequest;
import com.easypadel.dto.LoginResponse;
import com.easypadel.dto.UsuariCreateRequest;
import com.easypadel.dto.UsuariResponse;
import com.easypadel.dto.UsuariUpdateRequest;
import com.easypadel.entity.Usuari;
import com.easypadel.exception.BackendException;
import com.easypadel.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuariService {

    private final UsuariRepository usuariRepository;

    public UsuariService(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    public UsuariResponse create(UsuariCreateRequest request) {
        if (usuariRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BackendException(HttpStatus.CONFLICT, "Ja existeix un usuari amb l'email " + request.email());
        }

        Usuari nouUsuari = new Usuari();
        nouUsuari.setNom(request.nom());
        nouUsuari.setCognom(request.cognom());
        nouUsuari.setEmail(request.email().toLowerCase().trim());
        nouUsuari.setPassword(request.password());
        nouUsuari.setTelefon(request.telefon());
        nouUsuari.setRol(request.rol());
        nouUsuari.setActiu(true);

        Usuari guardat = usuariRepository.save(nouUsuari);
        return UsuariResponse.fromEntity(guardat);
    }

    public UsuariResponse update(Integer id, UsuariUpdateRequest request) {
        Usuari usuari = findById(id);

        if (usuariRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new BackendException(HttpStatus.CONFLICT, "Ja existeix un altre usuari amb l'email " + request.email());
        }

        usuari.setNom(request.nom());
        usuari.setCognom(request.cognom());
        usuari.setEmail(request.email().toLowerCase().trim());
        usuari.setTelefon(request.telefon());

        if (request.password() != null && !request.password().isBlank()) {
            usuari.setPassword(request.password());
        }
        if (request.rol() != null) {
            usuari.setRol(request.rol());
        }
        if (request.actiu() != null) {
            usuari.setActiu(request.actiu());
        }

        Usuari usuariobject = usuariRepository.save(usuari);
        return UsuariResponse.fromEntity(usuariobject);
    }

    public void delete(Integer id) {
        Usuari usuari = findById(id);
        usuariRepository.delete(usuari);
    }

    private Usuari findById(Integer id) {
        return usuariRepository.findById(id)
                .orElseThrow(() -> new BackendException(HttpStatus.NOT_FOUND, "Usuari no trobat amb id: " + id));
    }

    @Transactional(readOnly = true)
    public UsuariResponse getById(Integer id) {
        Usuari usuari = findById(id);
        return UsuariResponse.fromEntity(usuari);
    }

    @Transactional(readOnly = true)
    public List<UsuariResponse> listAllUsers() {
        return usuariRepository.findAll()
                .stream()
                .map(UsuariResponse::fromEntity)
                .toList();
    }

    //Metdoes propis de la autenticacio i canvi de contrasenya
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuari usuari = usuariRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new BackendException(HttpStatus.UNAUTHORIZED, "Credencials incorrectes"));

        //De moment es compara la contrasenya en text pla, sense cap mena de seguretat
        if (!usuari.getPassword().equals(request.password())) {
            throw new BackendException(HttpStatus.UNAUTHORIZED, "Credencials incorrectes");
        }

        if (!usuari.isActiu()) {
            throw new BackendException(HttpStatus.FORBIDDEN, "L'usuari està desactivat");
        }

        return LoginResponse.of(usuari);
    }

    public void changePassword(Integer id, CanviarPasswordRequest request) {
        Usuari usuari = findById(id);

        if (!usuari.getPassword().equals(request.passwordActual())) {
            throw new BackendException(HttpStatus.BAD_REQUEST, "La contrasenya actual no és correcta");
        }

        usuari.setPassword(request.novaPassword());
        usuariRepository.save(usuari);
    }
}
