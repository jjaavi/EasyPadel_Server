package com.easypadel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//Dto pel canvi de password d'un usuari. El pot utilitzar el soci o un admin.
public record CanviarPasswordRequest(
        @NotBlank
        String passwordActual,

        @NotBlank
        @Size(min = 4, max = 100)
        String novaPassword
) {
}
