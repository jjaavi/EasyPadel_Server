package com.easypadel.repository;

import com.easypadel.entity.Pista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PistaRepository extends JpaRepository<Pista, Integer> {

    List<Pista> findByActivaTrueOrderByIdAsc();

    boolean existsByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCaseAndIdNot(String nom, Integer id);
}
