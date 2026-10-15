package com.easypadel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PistaRequest(
        @NotBlank
        @Size(max = 20)
        String nom,

        Boolean activa
) {
}
