package com.easypadel.dto;

import java.time.LocalDate;
import java.util.List;

//DTO per a la disponibilitat d'una pista en una data concreta, amb els torns disponibles.
//L'app .NET podra utilitzar aquest DTO per a mostrar la disponibilitat de les pistes en una data concreta.
public record DisponibilitatPistaResponse(
        Integer pistaId,
        String pistaNom,
        LocalDate data,
        List<TornDisponibilitat> torns
) {
    public record TornDisponibilitat(
            int torn,
            boolean disponible
    ) {
    }
}
