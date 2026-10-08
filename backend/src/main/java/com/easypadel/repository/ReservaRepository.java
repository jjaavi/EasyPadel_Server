package com.easypadel.repository;

import com.easypadel.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    boolean existsByPistaIdAndDataAndTorn(Integer pistaId, LocalDate data, byte torn);

    boolean existsByUsuariIdAndDataAndTorn(Integer usuariId, LocalDate data, byte torn);

    boolean existsByPistaId(Integer pistaId);

    List<Reserva> findByUsuariIdAndDataGreaterThanEqualOrderByDataAscTornAsc(Integer usuariId, LocalDate desde);

    List<Reserva> findByUsuariIdOrderByDataDescTornAsc(Integer usuariId);

    List<Reserva> findByDataOrderByPistaIdAscTornAsc(LocalDate data);

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
