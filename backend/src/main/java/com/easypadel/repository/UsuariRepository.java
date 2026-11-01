package com.easypadel.repository;

import com.easypadel.entity.Usuari;
import com.easypadel.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuariRepository extends JpaRepository<Usuari, Integer> {

    //Comprovar si existeix el usuari amb el mail
    Optional<Usuari> findByEmailIgnoreCase(String email);

    //Retorna si o no existeix un usuari amb el mail
    boolean existsByEmailIgnoreCase(String email);

    //Retorna si existeix o no l'usuari amb mail i id diferent. S'utilitza per comprovar duplicats.
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);

    @Query("""
            select u from Usuari u
            where (:usuariId is null or u.id = :usuariId)
              and (:nom is null or lower(u.nom) = lower(:nom))
              and (:cognom is null or lower(u.cognom) = lower(:cognom))
              and (:email is null or lower(u.email) = lower(:email))
              and (:telefon is null or u.telefon = :telefon)
              and (:rol is null or u.rol = :rol)
              and (:actiu is null or u.actiu = :actiu)
              and (:dataAlta is null or u.dataAlta = :dataAlta)
            order by u.id asc
            """)
    List<Usuari> searchForAdmin(@Param("usuariId") Integer usuariId,
                                @Param("nom") String nom,
                                @Param("cognom") String cognom,
                                @Param("email") String email,
                                @Param("telefon") String telefon,
                                @Param("rol") Rol rol,
                                @Param("actiu") Boolean actiu,
                                @Param("dataAlta") LocalDate dataAlta);
}
