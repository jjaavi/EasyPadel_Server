package com.easypadel.dto;

import com.easypadel.entity.Pista;

public record PistaResponse(
        Integer id,
        String nom,
        boolean activa
) {
    public static PistaResponse fromEntity(Pista pista) {
        return new PistaResponse(
                pista.getId(),
                pista.getNom(),
                pista.isActiva()
        );
    }
}
