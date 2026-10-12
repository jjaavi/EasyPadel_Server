package com.easypadel.repository;

import com.easypadel.entity.Pista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PistaRepository extends JpaRepository<Pista, Integer> {

    List<Pista> findByActivaTrueOrderByIdAsc();

    //Comprova si existeix una pista pel nom
    boolean existsByNomIgnoreCase(String nom);

    //Comprova si existeix una pista ignorant el id. Serveix per validar duplicats i edicions.
    boolean existsByNomIgnoreCaseAndIdNot(String nom, Integer id);
}
