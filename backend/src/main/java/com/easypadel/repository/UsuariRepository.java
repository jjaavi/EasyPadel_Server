package com.easypadel.repository;

import com.easypadel.entity.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariRepository extends JpaRepository<Usuari, Integer> {

    //Comprovar si existeix el usuari amb el mail
    Optional<Usuari> findByEmailIgnoreCase(String email);

    //Retorna si o no existeix un usuari amb el mail
    boolean existsByEmailIgnoreCase(String email);

    //Retorna si existeix o no l'usuari amb mail i id diferent. S'utilitza per comprovar duplicats.
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);
}
