package com.easypadel.dto;

import com.easypadel.entity.Reserva;

import java.time.LocalDate;

public record ReservaResponse(
        Integer id,
        Integer usuariId,
        String usuariNom,
        String usuariCognom,
        Integer pistaId,
        String pistaNom,
        LocalDate data,
        int torn
) {
    public static ReservaResponse fromEntity(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getUsuari() != null ? reserva.getUsuari().getId() : null,
                reserva.getUsuari() != null ? reserva.getUsuari().getNom() : null,
                reserva.getUsuari() != null ? reserva.getUsuari().getCognom() : null,
                reserva.getPista() != null ? reserva.getPista().getId() : null,
                reserva.getPista() != null ? reserva.getPista().getNom() : null,
                reserva.getData(),
                reserva.getTorn()
        );
    }
}
