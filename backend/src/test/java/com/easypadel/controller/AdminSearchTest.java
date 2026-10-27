package com.easypadel.controller;

import com.easypadel.entity.Pista;
import com.easypadel.entity.Rol;
import com.easypadel.entity.Usuari;
import com.easypadel.exception.GlobalExceptionHandler;
import com.easypadel.repository.PistaRepository;
import com.easypadel.repository.UsuariRepository;
import com.easypadel.service.PistaService;
import com.easypadel.service.UsuariService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DataJpaTest(showSql = false, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import({UsuariService.class, PistaService.class})
class AdminSearchTest {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PistaRepository pistaRepository;

    @Autowired
    private UsuariService usuariService;

    @Autowired
    private PistaService pistaService;

    private MockMvc mockMvc;
    private Usuari usuari;
    private Pista pista;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new UsuariController(usuariService), new PistaController(pistaService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        usuari = new Usuari();
        usuari.setNom("Anna");
        usuari.setCognom("Garcia");
        usuari.setEmail("anna@example.com");
        usuari.setTelefon("123456789");
        usuari.setPassword("secret");
        usuari.setRol(Rol.ADMIN);
        usuari.setActiu(false);
        usuariRepository.saveAndFlush(usuari);

        Usuari altreUsuari = new Usuari();
        altreUsuari.setNom("Marc");
        altreUsuari.setCognom("Soler");
        altreUsuari.setEmail("marc@example.com");
        altreUsuari.setPassword("secret");
        usuariRepository.saveAndFlush(altreUsuari);

        pista = new Pista();
        pista.setNom("Pista 1");
        pista.setActiva(false);
        pistaRepository.saveAndFlush(pista);

        Pista altraPista = new Pista();
        altraPista.setNom("Pista 2");
        pistaRepository.saveAndFlush(altraPista);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/usuaris/cercaAdmin", "/api/usuaris/cercaAdmin/",
            "/api/pistes/cercaAdmin", "/api/pistes/cercaAdmin/"
    })
    void senseFiltresRetornaTotsElsRegistres(String ruta) throws Exception {
        mockMvc.perform(get(ruta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom").value(ruta.contains("usuaris") ? "Anna" : "Pista 1"))
                .andExpect(jsonPath("$[*].password").doesNotExist());
    }

    @Test
    void combinaTotsElsFiltresDUsuari() throws Exception {
        mockMvc.perform(get("/api/usuaris/cercaAdmin/")
                        .param("usuariId", usuari.getId().toString())
                        .param("nom", "ANNA")
                        .param("cognom", "GARCIA")
                        .param("email", "ANNA@EXAMPLE.COM")
                        .param("telefon", "123456789")
                        .param("rol", "ADMIN")
                        .param("actiu", "false")
                        .param("dataAlta", usuari.getDataAlta().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(usuari.getId()))
                .andExpect(jsonPath("$[0].actiu").value(false))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @ParameterizedTest
    @CsvSource({
            "nom, ANNA", "cognom, GARCIA", "email, ANNA@EXAMPLE.COM",
            "telefon, 123456789", "rol, ADMIN", "actiu, false"
    })
    void filtraUsuarisIndividualment(String filtre, String valor) throws Exception {
        mockMvc.perform(get("/api/usuaris/cercaAdmin").param(filtre, valor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(usuari.getId()));
    }

    @Test
    void combinaTotsElsFiltresDePista() throws Exception {
        mockMvc.perform(get("/api/pistes/cercaAdmin/")
                        .param("pistaId", pista.getId().toString())
                        .param("nom", "PISTA 1")
                        .param("activa", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(pista.getId()))
                .andExpect(jsonPath("$[0].activa").value(false));
    }

    @ParameterizedTest
    @CsvSource({"nom, PISTA 1", "activa, false"})
    void filtraPistesIndividualment(String filtre, String valor) throws Exception {
        mockMvc.perform(get("/api/pistes/cercaAdmin").param(filtre, valor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(pista.getId()));
    }

    @ParameterizedTest
    @CsvSource({
            "/api/usuaris/cercaAdmin, usuariId, 2147483647",
            "/api/usuaris/cercaAdmin, nom, Inexistent",
            "/api/usuaris/cercaAdmin, cognom, Inexistent",
            "/api/usuaris/cercaAdmin, email, inexistent@example.com",
            "/api/usuaris/cercaAdmin, telefon, 000000000",
            "/api/usuaris/cercaAdmin, dataAlta, 2000-01-01",
            "/api/pistes/cercaAdmin, pistaId, 2147483647",
            "/api/pistes/cercaAdmin, nom, Inexistent"
    })
    void senseCoincidenciesRetornaLlistaBuida(String ruta, String filtre, String valor) throws Exception {
        mockMvc.perform(get(ruta).param(filtre, valor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void filtresContradictorisNoRetornenCoincidencies() throws Exception {
        mockMvc.perform(get("/api/usuaris/cercaAdmin")
                        .param("nom", "Anna").param("actiu", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get("/api/pistes/cercaAdmin")
                        .param("nom", "Pista 1").param("activa", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @ParameterizedTest
    @CsvSource({
            "/api/usuaris/cercaAdmin, usuariId, abc",
            "/api/usuaris/cercaAdmin, rol, INVALID",
            "/api/usuaris/cercaAdmin, actiu, invalid",
            "/api/usuaris/cercaAdmin, dataAlta, 2026-99-99",
            "/api/pistes/cercaAdmin, pistaId, abc",
            "/api/pistes/cercaAdmin, activa, invalid"
    })
    void parametresInvalidsRetornen400(String ruta, String filtre, String valor) throws Exception {
        mockMvc.perform(get(ruta).param(filtre, valor))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
