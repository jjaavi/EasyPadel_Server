package com.easypadel.dto;

import com.easypadel.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//El UsuariUpdateRequest s'utilitzara per part del administrador.
public record UsuariUpdateRequest(
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

        @Size(min = 4, max = 100)
        String password,

        @Size(max = 15)
        String telefon,

        Rol rol,

        Boolean actiu
) {
}
