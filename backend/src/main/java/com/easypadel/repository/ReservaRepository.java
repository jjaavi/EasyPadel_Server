package com.easypadel.repository;

import com.easypadel.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    //Comprova si existeix una pista amb id, data i torn. Serveix per validar duplicats i edicions.
    boolean existsByPistaIdAndDataAndTorn(Integer pistaId, LocalDate data, byte torn);

    //Comprova si existeix una reserva amb usuari, data i torn. Serveix per validar duplicats i edicions.
    boolean existsByUsuariIdAndDataAndTorn(Integer usuariId, LocalDate data, byte torn);

    //Comprova si existeix una reserva amb usuari i data. Serveix per validar duplicats i edicions.
    boolean existsByUsuariIdAndData(Integer usuariId, LocalDate data);

    //Comprova la reserva amb el id de pista. Serveix per validar duplicats i edicions.
    boolean existsByPistaId(Integer pistaId);

    //Retorna la llista de reserves de un usuari a partir d'una data concreta, ordenades per data i torn ascendent.
    List<Reserva> findByUsuariIdAndDataGreaterThanEqualOrderByDataAscTornAsc(Integer usuariId, LocalDate desde);

    //Retorna la llista de reserves de un usuari ordenades per data.
    List<Reserva> findByUsuariIdOrderByDataDescTornAsc(Integer usuariId);

    //Retorna la llista de reserves d'una data concreta ordenades per pista.
    List<Reserva> findByDataOrderByPistaIdAscTornAsc(LocalDate data);

    //Retorna la llista de reserves d'una pista concreta i una data concreta ordenades per torn.
    List<Reserva> findByPistaIdAndData(Integer pistaId, LocalDate data);

    // Filtres opcionals per a la consulta d'administrador
    @Query("""
            select r from Reserva r
            where (:data is null or r.data = :data)
              and (:pistaId is null or r.pista.id = :pistaId)
              and (:usuariId is null or r.usuari.id = :usuariId)
            order by r.data asc, r.torn asc, r.pista.id asc
            """)
    List<Reserva> cercar(@Param("data") LocalDate data,
                         @Param("pistaId") Integer pistaId,
                         @Param("usuariId") Integer usuariId);
}
