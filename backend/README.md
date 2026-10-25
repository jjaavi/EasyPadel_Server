# EasyPadel - API REST

API REST del servidor d'EasyPadel (Spring Boot 3 + MySQL), consumida per
l'aplicacio d'escriptori .NET.

- URL base: `http://localhost:8080`
- Format: JSON (`Content-Type: application/json`)
- Dates: format ISO `yyyy-MM-dd` (p. ex. `2026-10-14`)
- Seguretat: de moment no hi ha autenticacio als endpoints i les contrasenyes
  es guarden i comparen en text pla.

## Index

- [Errors](#errors)
- [Estat del servidor](#estat-del-servidor)
- [Usuaris](#usuaris)
- [Pistes](#pistes)
- [Reserves](#reserves)

---

## Errors

Tots els errors retornen el mateix format:

```json
{ "status": 404, "missatge": "Usuari no trobat amb id: 7" }
```

| HTTP | Quan es produeix |
|------|------------------|
| 400 | Cos de la peticio no valid, camps obligatoris buits o amb format incorrecte, parametres no valids, o data de reserva fora de rang. |
| 401 | Credencials incorrectes al login. |
| 403 | Usuari desactivat o pista desactivada. |
| 404 | Recurs (usuari, pista o reserva) no trobat. |
| 409 | Conflicte: email o nom de pista duplicat, torn ja reservat, o pista amb reserves que es vol eliminar. |
| 500 | Error intern del servidor. |

---

## Estat del servidor

### `GET /status`

Permet comprovar des de l'aplicacio d'escriptori si el servidor ha completat
l'arrencada.

| HTTP | JSON | Significat |
|------|------|------------|
| 200 | `{"status":"UP"}` | Spring Boot ha completat l'arrencada, inclosos els `ApplicationRunner`. |
| 503 | `{"status":"DOWN"}` | L'aplicacio encara no esta preparada o ha deixat d'acceptar peticions. |

El client .NET ha d'iniciar el JAR com a proces independent i consultar aquest
endpoint periodicament, amb un temps limit d'espera. Durant l'arrencada tambe
pot haver-hi errors de connexio HTTP si el port encara no esta disponible.
Si el proces acaba abans de rebre un 200, cal tractar-ho com un error d'arrencada.

Aquest endpoint informa de l'estat de disponibilitat de Spring Boot; no executa
una consulta a MySQL a cada peticio ni detecta per si sol caigudes posteriors
de la base de dades.

---

## Usuaris

Base: `/api/usuaris`

| Metode | Ruta | Descripcio | Resposta OK |
|--------|------|------------|-------------|
| POST | `/api/usuaris/login` | Inici de sessio | 200 `LoginResponse` |
| POST | `/api/usuaris` | Crear usuari | 201 `UsuariResponse` |
| GET | `/api/usuaris` | Llistar tots els usuaris | 200 `UsuariResponse[]` |
| GET | `/api/usuaris/{id}` | Obtenir un usuari | 200 `UsuariResponse` |
| PUT | `/api/usuaris/{id}` | Modificar un usuari | 200 `UsuariResponse` |
| PUT | `/api/usuaris/{id}/password` | Canviar la contrasenya | 204 sense cos |
| DELETE | `/api/usuaris/{id}` | Eliminar un usuari | 204 sense cos |

### `POST /api/usuaris/login`

Comprova l'email (sense distingir majuscules) i la contrasenya contra la taula
`usuaris`.

Peticio:

```json
{ "email": "marc.garcia@gmail.com", "password": "1234" }
```

| Camp | Tipus | Validacio |
|------|-------|-----------|
| `email` | string | Obligatori, format email. |
| `password` | string | Obligatori. |

Resposta 200:

```json
{
  "correcte": true,
  "idUsuari": 1,
  "nom": "Marc",
  "cognom": "Garcia",
  "email": "marc.garcia@gmail.com",
  "rol": "ADMIN",
  "missatge": "Inici de sessio correcte"
}
```

| HTTP | Significat |
|------|------------|
| 200 | Login correcte. |
| 400 | Email o password buits o amb format no valid. |
| 401 | `Credencials incorrectes`: l'email no existeix o la contrasenya no coincideix. |
| 403 | `L'usuari està desactivat`: credencials correctes pero l'usuari no esta actiu. |

L'app .NET ha de considerar el login correcte nomes amb un HTTP 200 i guardar
`idUsuari` per utilitzar-lo a la resta d'endpoints (reserves, perfil, etc.).

### `POST /api/usuaris`

```json
{
  "nom": "Marc",
  "cognom": "Garcia",
  "email": "marc.garcia@gmail.com",
  "password": "1234",
  "telefon": "648213759",
  "rol": "USER"
}
```

| Camp | Tipus | Validacio |
|------|-------|-----------|
| `nom` | string | Obligatori, max. 20. |
| `cognom` | string | Obligatori, max. 20. |
| `email` | string | Obligatori, format email, max. 100. Es guarda en minuscules. |
| `password` | string | Obligatori, entre 4 i 100. |
| `telefon` | string | Opcional, max. 15. |
| `rol` | string | Obligatori: `ADMIN` o `USER`. |

L'usuari es crea actiu i amb `dataAlta` = avui.
Errors: 400 (validacio), 409 (email ja existent).

### `PUT /api/usuaris/{id}`

Pensat per a l'administrador.

```json
{
  "nom": "Marc",
  "cognom": "Garcia",
  "email": "marc.garcia@gmail.com",
  "password": null,
  "telefon": "648213759",
  "rol": "ADMIN",
  "actiu": true
}
```

| Camp | Tipus | Validacio |
|------|-------|-----------|
| `nom` | string | Obligatori, max. 20. |
| `cognom` | string | Obligatori, max. 20. |
| `email` | string | Obligatori, format email, max. 100. |
| `password` | string | Opcional, entre 4 i 100. Si es buit o `null` no es modifica. |
| `telefon` | string | Opcional, max. 15. |
| `rol` | string | Opcional: `ADMIN` o `USER`. Si es `null` no es modifica. |
| `actiu` | boolean | Opcional. Si es `null` no es modifica. |

Errors: 400, 404 (usuari no trobat), 409 (email d'un altre usuari).

### `PUT /api/usuaris/{id}/password`

```json
{ "passwordActual": "1234", "novaPassword": "abcd" }
```

| Camp | Tipus | Validacio |
|------|-------|-----------|
| `passwordActual` | string | Obligatori. Ha de coincidir amb la contrasenya actual. |
| `novaPassword` | string | Obligatori, entre 4 i 100. |

Errors: 400 (validacio o contrasenya actual incorrecta), 404.

### `GET /api/usuaris`, `GET /api/usuaris/{id}`, `DELETE /api/usuaris/{id}`

`GET /api/usuaris/{id}` i `DELETE /api/usuaris/{id}` retornen 404 si l'usuari no existeix.

### `UsuariResponse`

```json
{
  "id": 1,
  "nom": "Marc",
  "cognom": "Garcia",
  "email": "marc.garcia@gmail.com",
  "telefon": "648213759",
  "rol": "ADMIN",
  "actiu": true,
  "dataAlta": "2026-10-01"
}
```

La contrasenya mai es retorna.

---

## Pistes

Base: `/api/pistes`

| Metode | Ruta | Descripcio | Resposta OK |
|--------|------|------------|-------------|
| POST | `/api/pistes` | Crear pista | 201 `PistaResponse` |
| GET | `/api/pistes` | Llistar totes les pistes | 200 `PistaResponse[]` |
| GET | `/api/pistes/actives` | Llistar les pistes actives (ordenades per id) | 200 `PistaResponse[]` |
| GET | `/api/pistes/{id}` | Obtenir una pista | 200 `PistaResponse` |
| PUT | `/api/pistes/{id}` | Modificar una pista | 200 `PistaResponse` |
| DELETE | `/api/pistes/{id}` | Eliminar una pista | 204 sense cos |

### Peticio (`POST` i `PUT`)

```json
{ "nom": "Pista 1", "activa": true }
```

| Camp | Tipus | Validacio |
|------|-------|-----------|
| `nom` | string | Obligatori, max. 20. Unic (sense distingir majuscules). |
| `activa` | boolean | Opcional. En crear, per defecte `true`; en modificar, si es `null` no es canvia. |

Errors:

- `POST`: 400, 409 (nom duplicat).
- `PUT`: 400, 404, 409 (nom d'una altra pista).
- `DELETE`: 404, 409 (la pista te reserves associades).
- `GET /{id}`: 404.

### `PistaResponse`

```json
{ "id": 1, "nom": "Pista 1", "activa": true }
```

---

## Reserves

Base: `/api/reserves`

Cada dia es divideix en torns numerats de l'**1 al 7**. Una pista nomes pot
tenir una reserva per data i torn.

| Metode | Ruta | Descripcio | Resposta OK |
|--------|------|------------|-------------|
| POST | `/api/reserves` | Crear reserva | 201 `ReservaResponse` |
| GET | `/api/reserves` | Llistar totes les reserves | 200 `ReservaResponse[]` |
| GET | `/api/reserves/{id}` | Obtenir una reserva | 200 `ReservaResponse` |
| PUT | `/api/reserves/{id}` | Modificar una reserva | 200 `ReservaResponse` |
| DELETE | `/api/reserves/{id}` | Eliminar una reserva | 204 sense cos |
| GET | `/api/reserves/usuari/{usuariId}` | Totes les reserves d'un usuari | 200 `ReservaResponse[]` |
| GET | `/api/reserves/usuari/{usuariId}/posteriors` | Reserves d'un usuari des d'avui | 200 `ReservaResponse[]` |
| GET | `/api/reserves/usuari/{usuariId}/anteriors?data=` | Reserves d'un usuari anteriors a `data` | 200 `ReservaResponse[]` |
| GET | `/api/reserves/data/{data}` | Reserves d'un dia | 200 `ReservaResponse[]` |
| GET | `/api/reserves/properes?data=` | Properes 20 reserves des de `data` | 200 `ReservaResponse[]` |
| GET | `/api/reserves/pista/{pistaId}?data=` | Reserves d'una pista en un dia | 200 `ReservaResponse[]` |
| GET | `/api/reserves/cercaAdmin?reservaId=&usuariId=&pistaId=&data=&torn=` | Cerca per a l'administrador | 200 `ReservaResponse[]` |
| GET | `/api/reserves/disponibilitat/{pistaId}?data=` | Disponibilitat d'una pista per torns | 200 `DisponibilitatPistaResponse` |

### Peticio (`POST` i `PUT`)

```json
{ "usuariId": 1, "pistaId": 2, "data": "2026-10-22", "torn": 3 }
```

| Camp | Tipus | Validacio |
|------|-------|-----------|
| `usuariId` | int | Obligatori. L'usuari ha d'existir i estar actiu. |
| `pistaId` | int | Obligatori. La pista ha d'existir i estar activa. |
| `data` | date | Obligatori. El rang de dates permes es controla a l'aplicacio d'interficie. |
| `torn` | int | Obligatori, entre 1 i 7. El torn ha d'estar lliure. |

Errors:

- 400: validacio.
- 403: usuari desactivat o pista desactivada.
- 404: reserva (`PUT`), usuari o pista no trobats.
- 409: `El torn seleccionat ja està reservat`. En `PUT` no es te en compte la propia reserva.

### Consultes

| Ruta | Parametres | Ordre | Errors |
|------|------------|-------|--------|
| `/usuari/{usuariId}` | - | Data desc., torn asc. | 404 usuari |
| `/usuari/{usuariId}/posteriors` | - | Data asc., torn asc. Inclou avui. | 404 usuari |
| `/usuari/{usuariId}/anteriors` | `data` (obligatori) | Data desc., torn desc. Exclou `data`. | 400, 404 usuari |
| `/data/{data}` | - | Pista asc., torn asc. | 400 |
| `/properes` | `data` (obligatori) | Data asc., torn asc., pista asc. Inclou `data`. Maxim 20. | 400 |
| `/pista/{pistaId}` | `data` (obligatori) | Torn asc. | 400, 404 pista |
| `/cercaAdmin` | `reservaId`, `usuariId`, `pistaId`, `data`, `torn` (tots opcionals) | - | 400 (inclou `torn` fora de 1-7), 404 usuari/pista |
| `/disponibilitat/{pistaId}` | `data` (obligatori) | Torn asc. | 400, 403 pista desactivada, 404 pista |

Exemples:

```
GET /api/reserves/usuari/1/anteriors?data=2026-10-20
GET /api/reserves/pista/2?data=2026-10-22
GET /api/reserves/cercaAdmin?usuariId=1&data=2026-10-22
GET /api/reserves/cercaAdmin?reservaId=10
GET /api/reserves/disponibilitat/2?data=2026-10-22
```

Els filtres de `/cercaAdmin` es combinen amb AND. Si cap reserva coincideix, retorna una llista buida, tambe si `reservaId` no existeix.

### `ReservaResponse`

```json
{
  "id": 10,
  "usuariId": 1,
  "usuariNom": "Marc",
  "usuariCognom": "Garcia",
  "pistaId": 2,
  "pistaNom": "Pista 2",
  "data": "2026-10-22",
  "torn": 3
}
```

### `DisponibilitatPistaResponse`

```json
{
  "pistaId": 2,
  "pistaNom": "Pista 2",
  "data": "2026-10-22",
  "torns": [
    { "torn": 1, "disponible": true },
    { "torn": 2, "disponible": false },
    { "torn": 3, "disponible": true },
    { "torn": 4, "disponible": true },
    { "torn": 5, "disponible": true },
    { "torn": 6, "disponible": true },
    { "torn": 7, "disponible": true }
  ]
}
```
