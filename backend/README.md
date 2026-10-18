# Estat del servidor

`GET /status` permet comprovar des de l'aplicacio d'escriptori si el servidor
ha completat l'arrencada.

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
