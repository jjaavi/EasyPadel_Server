package com.easypadel.dto;

import com.easypadel.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuariCreateRequest(
        @NotBlank
        @Size(max = 20)
        String nom,

        @NotBlank
        @Size(max = 20)
        String cognom,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(min = 4, max = 100)
        String password,

        @Size(max = 15)
        String telefon,

        @NotNull
        Rol rol
) {
}
