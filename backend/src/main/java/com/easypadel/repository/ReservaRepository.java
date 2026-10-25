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

    boolean existsByPistaIdAndDataAndTornAndIdNot(Integer pistaId, LocalDate data, byte torn, Integer id);

    //Comprova la reserva amb el id de pista. Serveix per validar duplicats i edicions.
    boolean existsByPistaId(Integer pistaId);

    //Retorna la llista de reserves de un usuari a partir d'una data concreta, ordenades per data i torn ascendent.
    List<Reserva> findByUsuariIdAndDataGreaterThanEqualOrderByDataAscTornAsc(Integer usuariId, LocalDate desde);

    //Retorna la llista de reserves de un usuari a partir d'una data concreta, ordenades per data i torn descendent.
    List<Reserva> findByUsuariIdAndDataBeforeOrderByDataDescTornDesc(Integer usuariId, LocalDate data);

    //Retorna la llista de reserves de un usuari ordenades per data.
    List<Reserva> findByUsuariIdOrderByDataDescTornAsc(Integer usuariId);

    //Retorna la llista de reserves d'una data concreta ordenades per pista.
    List<Reserva> findByDataOrderByPistaIdAscTornAsc(LocalDate data);

    //Retorna les 20 properes reserves a partir d'una data, ordenades per data, torn i pista ascendent.
    List<Reserva> findTop20ByDataGreaterThanEqualOrderByDataAscTornAscPistaIdAsc(LocalDate data);

    //Retorna la llista de reserves d'una pista concreta i una data concreta ordenades per torn.
    List<Reserva> findByPistaIdAndData(Integer pistaId, LocalDate data);

    // Filtres opcionals per a la consulta d'administrador
    @Query("""
            select r from Reserva r
            where (:reservaId is null or r.id = :reservaId)
              and (:usuariId is null or r.usuari.id = :usuariId)
              and (:pistaId is null or r.pista.id = :pistaId)
              and (:data is null or r.data = :data)
              and (:torn is null or r.torn = :torn)
            order by r.data asc, r.torn asc, r.pista.id asc
            """)
    List<Reserva> searchForAdmin(@Param("reservaId") Integer reservaId,
                         @Param("usuariId") Integer usuariId,
                         @Param("pistaId") Integer pistaId,
                         @Param("data") LocalDate data,
                         @Param("torn") Byte torn);
}
