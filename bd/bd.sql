-- ============================================================
-- Script inicial de la base de dades de EASYPADEL
-- ============================================================

CREATE DATABASE IF NOT EXISTS easy_padel;

USE easy_padel;

-- Es desactiven les foreign keys per poder eliminar les taules
-- en el cas de que es torni a executar l'escript.
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS reserves;
DROP TABLE IF EXISTS pistes;
DROP TABLE IF EXISTS usuaris;

SET FOREIGN_KEY_CHECKS = 1;

-- =======================Taula usuaris=======================
CREATE TABLE usuaris (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nom       VARCHAR(20)  NOT NULL,
    cognom    VARCHAR(20)  NOT NULL,
    email     VARCHAR(100) NOT NULL,
    password  VARCHAR(100) NOT NULL,
    telefon   VARCHAR(15)  NULL,
    rol       ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
    actiu     BOOLEAN      NOT NULL DEFAULT TRUE,
    data_alta DATE         NOT NULL DEFAULT (CURRENT_DATE),

    CONSTRAINT uk_usuaris_email UNIQUE (email)
);

-- =======================Taula pistes=======================
CREATE TABLE pistes (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nom    VARCHAR(20) NOT NULL,
    activa BOOLEAN     NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_pistes_nom UNIQUE (nom)
);

-- =======================Taula reserves=======================
CREATE TABLE reserves (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    usuari_id  INT     NOT NULL,
    pista_id   INT     NOT NULL,
    data       DATE    NOT NULL,
    torn       TINYINT NOT NULL,

    CONSTRAINT ck_reserves_torn
        CHECK (torn BETWEEN 1 AND 7),

    CONSTRAINT fk_reserves_usuari
        FOREIGN KEY (usuari_id) REFERENCES usuaris(id),

    CONSTRAINT fk_reserves_pista
        FOREIGN KEY (pista_id) REFERENCES pistes(id),

    CONSTRAINT uk_reserva_unica
        UNIQUE (pista_id, data, torn),

    INDEX idx_reserves_usuari_data (usuari_id, data)
);