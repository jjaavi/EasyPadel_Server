-- ============================================================
-- Script inicial de la base de dades de EASYPADEL
-- ============================================================

USE easy_padel;

-- Ordres per borrar les dades de prova
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE reserves;
-- TRUNCATE TABLE pistes;
-- TRUNCATE TABLE usuaris;
-- SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO usuaris (nom, cognom, email, password, telefon, rol) VALUES
('Marc',  'Garcia',  'marc.garcia@gmail.com',  '$2a$10$zFrAh2EwzE5CrcPe7g5feuGx3SHaDCRXohA0zr4SPkN7PJ2eW5Rtq', '648213759', 'ADMIN'),
('Laura', 'Pons',    'laura.pons@gmail.com',   '$2a$10$s/PqZaCp5B.Z1hhExuOqdu1UpWH/5/VXpu/f9q4h4fK.1vPRhavD2', '671934085', 'ADMIN'),
('Pau',   'Serra',   'pau.serra@gmail.com',    '$2a$10$9uYJKl8hHw4k6TIygUvdYOvuIfW2pyv3FBZs11Z/O3WdLccQqEX6e', '625708412', 'ADMIN'),
('Anna',  'Vidal',   'anna.vidal@gmail.com',   '$2a$10$akinL9uc.Idg.FPHqv74kOJrLIJzZ./86puxpZn3SgQahY81loOgm', '693156270', 'ADMIN'),
('Joan',  'Ferrer',  'joan.ferrer@gmail.com',  '$2a$10$BSmWZg0sL7QorToKWqAI9OvkvQOJEVoUYishvTy3zf4z/wNqDd8LO', '657342891', 'ADMIN');

INSERT INTO pistes (nom, activa) VALUES
('Pista 1',  TRUE),
('Pista 2',  TRUE),
('Pista 3',  TRUE),
('Pista 4',  TRUE),
('Pista 5',  TRUE),
('Pista 6',  TRUE),
('Pista 7',  TRUE),
('Pista 8',  TRUE),
('Pista 9',  TRUE),
('Pista 10', FALSE);

INSERT INTO reserves (usuari_id, pista_id, data, torn)
SELECT u.id, p.id, DATE_ADD(CURRENT_DATE, INTERVAL prova.dies DAY), prova.torn
FROM (
    SELECT 'marc.garcia@gmail.com' AS email, 'Pista 1' AS pista, -3 AS dies, 1 AS torn
    UNION ALL SELECT 'marc.garcia@gmail.com', 'Pista 2', 1, 3
    UNION ALL SELECT 'laura.pons@gmail.com', 'Pista 3', -2, 2
    UNION ALL SELECT 'laura.pons@gmail.com', 'Pista 4', 2, 4
    UNION ALL SELECT 'pau.serra@gmail.com', 'Pista 5', -1, 3
    UNION ALL SELECT 'pau.serra@gmail.com', 'Pista 6', 3, 5
    UNION ALL SELECT 'anna.vidal@gmail.com', 'Pista 7', 0, 4
    UNION ALL SELECT 'anna.vidal@gmail.com', 'Pista 8', 4, 6
    UNION ALL SELECT 'joan.ferrer@gmail.com', 'Pista 9', 0, 5
    UNION ALL SELECT 'joan.ferrer@gmail.com', 'Pista 1', 5, 7
) AS prova
JOIN usuaris u ON u.email = prova.email
JOIN pistes p ON p.nom = prova.pista;


-- ============================================================


