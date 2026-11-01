package com.easypadel.repository;

import com.easypadel.entity.Pista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PistaRepository extends JpaRepository<Pista, Integer> {

    List<Pista> findByActivaTrueOrderByIdAsc();

    //Comprova si existeix una pista pel nom
    boolean existsByNomIgnoreCase(String nom);

    //Comprova si existeix una pista ignorant el id. Serveix per validar duplicats i edicions.
    boolean existsByNomIgnoreCaseAndIdNot(String nom, Integer id);

    @Query("""
            select p from Pista p
            where (:pistaId is null or p.id = :pistaId)
              and (:nom is null or lower(p.nom) = lower(:nom))
              and (:activa is null or p.activa = :activa)
            order by p.id asc
            """)
    List<Pista> searchForAdmin(@Param("pistaId") Integer pistaId,
                               @Param("nom") String nom,
                               @Param("activa") Boolean activa);
}
