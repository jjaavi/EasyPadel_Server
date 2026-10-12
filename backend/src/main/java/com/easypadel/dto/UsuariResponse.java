package com.easypadel.dto;

import com.easypadel.entity.Rol;
import com.easypadel.entity.Usuari;

import java.time.LocalDate;

public record UsuariResponse(Integer id, String nom, String cognom, String email, String telefon, Rol rol, boolean actiu, LocalDate dataAlta) {
    public static UsuariResponse fromEntity(Usuari usuari) {
        return new UsuariResponse(
                usuari.getId(),
                usuari.getNom(),
                usuari.getCognom(),
                usuari.getEmail(),
                usuari.getTelefon(),
                usuari.getRol(),
                usuari.isActiu(),
                usuari.getDataAlta()
        );
    }
}
