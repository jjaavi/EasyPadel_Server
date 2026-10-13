package com.easypadel.dto;

public record LoginResponse(
        UsuariResponse usuari,
        String missatge
) {
    public static LoginResponse of(UsuariResponse usuari) {
        return new LoginResponse(usuari, "Inici de sessio correcte");
    }
}
