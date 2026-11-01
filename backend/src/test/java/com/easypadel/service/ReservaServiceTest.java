package com.easypadel.service;

import com.easypadel.dto.ReservaResponse;
import com.easypadel.entity.Pista;
import com.easypadel.entity.Reserva;
import com.easypadel.entity.Usuari;
import com.easypadel.exception.BackendException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
}, showSql = false)
@Import(ReservaService.class)
class ReservaServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaService reservaService;

    @Test
    void listReservesByPistaIncludesTodayAndFutureOnlyInExistingOrder() {
        LocalDate avui = LocalDate.now();
        Pista pista = createPista("Pista 1");
        pista.setActiva(false);
        Pista altraPista = createPista("Pista 2");
        Usuari usuari = new Usuari();
        usuari.setNom("Test");
        usuari.setCognom("Test");
        usuari.setEmail("test@example.com");
        usuari.setPassword("test");
        entityManager.persist(usuari);

        createReserva(pista, usuari, avui.minusDays(1), 1);
        createReserva(altraPista, usuari, avui.plusDays(1), 1);
        Reserva avuiTorn3 = createReserva(pista, usuari, avui, 3);
        Reserva futura = createReserva(pista, usuari, avui.plusDays(1), 2);
        Reserva avuiTorn1 = createReserva(pista, usuari, avui, 1);
        entityManager.flush();

        List<ReservaResponse> result = reservaService.listReservesByPista(pista.getId());

        assertThat(result).extracting(ReservaResponse::id)
                .containsExactly(futura.getId(), avuiTorn1.getId(), avuiTorn3.getId());
        assertThat(result).allSatisfy(reserva -> {
            assertThat(reserva.pistaId()).isEqualTo(pista.getId());
            assertThat(reserva.data()).isAfterOrEqualTo(avui);
        });
        assertThat(reservaService.listReservesByPistaAndData(pista.getId(), avui.minusDays(1)))
                .hasSize(1);
    }

    @Test
    void listReservesByPistaReturnsEmptyListWhenNoReservationsExist() {
        Pista pista = createPista("Pista buida");

        assertThat(reservaService.listReservesByPista(pista.getId())).isEmpty();
    }

    @Test
    void listReservesByPistaReturnsNotFoundForUnknownPista() {
        assertThatThrownBy(() -> reservaService.listReservesByPista(Integer.MAX_VALUE))
                .isInstanceOfSatisfying(BackendException.class,
                        exception -> assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    private Pista createPista(String nom) {
        Pista pista = new Pista();
        pista.setNom(nom);
        return entityManager.persist(pista);
    }

    private Reserva createReserva(Pista pista, Usuari usuari, LocalDate data, int torn) {
        Reserva reserva = new Reserva();
        reserva.setPista(pista);
        reserva.setUsuari(usuari);
        reserva.setData(data);
        reserva.setTorn(torn);
        return entityManager.persist(reserva);
    }
}
