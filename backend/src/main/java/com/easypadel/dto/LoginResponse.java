package com.easypadel.dto;

import com.easypadel.entity.Rol;
import com.easypadel.entity.Usuari;

//DTO de resposta del login.
//Si es correcte retorna les dades de l'usuari i un missatge d'èxit.
public record LoginResponse(
        boolean correcte,
        Integer idUsuari,
        String nom,
        String cognom,
        String email,
        Rol rol,
        String missatge
) {
    public static LoginResponse of(Usuari usuari) {
        return new LoginResponse(
                true,
                usuari.getId(),
                usuari.getNom(),
                usuari.getCognom(),
                usuari.getEmail(),
                usuari.getRol(),
                "Inici de sessio correcte"
        );
    }
}
