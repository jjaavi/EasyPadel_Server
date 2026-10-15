package com.easypadel.dto;

import com.easypadel.entity.Reserva;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaRequest(
        @NotNull
        Integer usuariId,

        @NotNull
        Integer pistaId,

        @NotNull
        LocalDate data,

        @NotNull
        @Min(Reserva.TORN_MIN)
        @Max(Reserva.TORN_MAX)
        Integer torn
) {
}
