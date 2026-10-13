package com.easypadel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//DTO per poder fer el login.
public record LoginRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
