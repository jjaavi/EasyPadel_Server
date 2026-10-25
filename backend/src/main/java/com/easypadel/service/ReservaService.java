package com.easypadel.service;

import com.easypadel.dto.DisponibilitatPistaResponse;
import com.easypadel.dto.ReservaRequest;
import com.easypadel.dto.ReservaResponse;
import com.easypadel.entity.Pista;
import com.easypadel.entity.Reserva;
import com.easypadel.entity.Usuari;
import com.easypadel.exception.BackendException;
import com.easypadel.repository.PistaRepository;
import com.easypadel.repository.ReservaRepository;
import com.easypadel.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuariRepository usuariRepository;
    private final PistaRepository pistaRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuariRepository usuariRepository,
            PistaRepository pistaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuariRepository = usuariRepository;
        this.pistaRepository = pistaRepository;
    }

    public ReservaResponse create(ReservaRequest request) {
        //1. Primer es valida que l'usuari existeix
        Usuari usuari = findUsuariById(request.usuariId());
        validarUsuariActiu(usuari);
        //2. Es valida que la pista exiteix
        Pista pista = findPistaById(request.pistaId());
        //3. Es valida que la pista esta activa
        validarPistaActiva(pista);
        //4. Es valida que la pista esta disponible en la data i torn indicats
        validarDisponibilitat(pista.getId(), request.data(), request.torn(), null);

        Reserva reserva = new Reserva();
        reserva.setUsuari(usuari);
        reserva.setPista(pista);
        reserva.setData(request.data());
        reserva.setTorn(request.torn());

        return ReservaResponse.fromEntity(reservaRepository.save(reserva));
    }

    //El update funciona igual que el create, pero validant que la reserva existeix previament
    public ReservaResponse update(Integer id, ReservaRequest request) {
        //Es valida que la reserva existeix i es recupera de la base de dades
        Reserva reserva = findById(id);

        Usuari usuari = findUsuariById(request.usuariId());
        validarUsuariActiu(usuari);
        Pista pista = findPistaById(request.pistaId());
        validarPistaActiva(pista);
        validarDisponibilitat(pista.getId(), request.data(), request.torn(), id);

        reserva.setUsuari(usuari);
        reserva.setPista(pista);
        reserva.setData(request.data());
        reserva.setTorn(request.torn());

        return ReservaResponse.fromEntity(reservaRepository.save(reserva));
    }

    public void delete(Integer id) {
        Reserva reserva = findById(id);
        reservaRepository.delete(reserva);
    }

    private Reserva findById(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new BackendException(HttpStatus.NOT_FOUND, "Reserva no trobada amb id: " + id));
    }

    @Transactional(readOnly = true)
    public ReservaResponse getById(Integer id) {
        return ReservaResponse.fromEntity(findById(id));
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listAllReservas() {
        return reservaRepository.findAll()
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    //Metode per veure totes les reserves
    @Transactional(readOnly = true)
    public List<ReservaResponse> listReservesByUsuari(Integer usuariId) {
        findUsuariById(usuariId);
        return reservaRepository.findByUsuariIdOrderByDataDescTornAsc(usuariId)
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    //Aquest metode sera el que el soci veura a la seva pantalla de reserves
    @Transactional(readOnly = true)
    public List<ReservaResponse> listReservesPosteriorsPerUsuariId(Integer usuariId) {
        findUsuariById(usuariId);
        return reservaRepository.findByUsuariIdAndDataGreaterThanEqualOrderByDataAscTornAsc(
                        usuariId, LocalDate.now())
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    //Aquest metode sera per a que el soci vegi les reserves ja completes a la data actual
    @Transactional(readOnly = true)
    public List<ReservaResponse> listReservesAnteriorsPerUsuariId(Integer usuariId, LocalDate data) {
        findUsuariById(usuariId);
        return reservaRepository.findByUsuariIdAndDataBeforeOrderByDataDescTornDesc(usuariId, data)
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    //Retorna un maxim de 20 reserves des de la data indicada fins a la mes llunyana
    @Transactional(readOnly = true)
    public List<ReservaResponse> listProperes20Reserves(LocalDate data) {
        return reservaRepository.findTop20ByDataGreaterThanEqualOrderByDataAscTornAscPistaIdAsc(data)
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listReservesByData(LocalDate data) {
        return reservaRepository.findByDataOrderByPistaIdAscTornAsc(data)
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listReservesByPistaAndData(Integer pistaId, LocalDate data) {
        findPistaById(pistaId);
        return reservaRepository.findByPistaIdAndData(pistaId, data)
                .stream()
                .sorted((a, b) -> Integer.compare(a.getTorn(), b.getTorn()))
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> searchForAdmin(
            Integer reservaId, Integer usuariId, Integer pistaId, LocalDate data, Integer torn) {
        if (usuariId != null) {
            findUsuariById(usuariId);
        }
        if (pistaId != null) {
            findPistaById(pistaId);
        }
        if (torn != null && (torn < Reserva.TORN_MIN || torn > Reserva.TORN_MAX)) {
            throw new BackendException(HttpStatus.BAD_REQUEST,
                    "El torn ha d'estar entre " + Reserva.TORN_MIN + " i " + Reserva.TORN_MAX);
        }
        Byte tornFiltre = torn == null ? null : torn.byteValue();
        return reservaRepository.searchForAdmin(reservaId, usuariId, pistaId, data, tornFiltre)
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisponibilitatPistaResponse getDisponibilitat(Integer pistaId, LocalDate data) {
        Pista pista = findPistaById(pistaId);
        validarPistaActiva(pista);

        Set<Integer> tornsOcupats = reservaRepository.findByPistaIdAndData(pistaId, data)
                .stream()
                .map(Reserva::getTorn)
                .collect(Collectors.toSet());

        List<DisponibilitatPistaResponse.TornDisponibilitat> torns =
                IntStream.rangeClosed(Reserva.TORN_MIN, Reserva.TORN_MAX)
                        .mapToObj(torn -> new DisponibilitatPistaResponse.TornDisponibilitat(
                                torn, !tornsOcupats.contains(torn)))
                        .toList();

        return new DisponibilitatPistaResponse(pista.getId(), pista.getNom(), data, torns);
    }

    private void validarDisponibilitat(Integer pistaId, LocalDate data, Integer torn, Integer reservaIdIgnorada) {
        boolean ocupat = reservaIdIgnorada == null
                ? reservaRepository.existsByPistaIdAndDataAndTorn(pistaId, data, torn.byteValue())
                : reservaRepository.existsByPistaIdAndDataAndTornAndIdNot(
                        pistaId, data, torn.byteValue(), reservaIdIgnorada);

        if (ocupat) {
            throw new BackendException(HttpStatus.CONFLICT, "El torn seleccionat ja està reservat");
        }
    }

    private void validarUsuariActiu(Usuari usuari) {
        if (!usuari.isActiu()) {
            throw new BackendException(HttpStatus.FORBIDDEN, "L'usuari està desactivat");
        }
    }

    private void validarPistaActiva(Pista pista) {
        if (!pista.isActiva()) {
            throw new BackendException(HttpStatus.FORBIDDEN, "La pista està desactivada");
        }
    }

    private Usuari findUsuariById(Integer id) {
        return usuariRepository.findById(id)
                .orElseThrow(() -> new BackendException(HttpStatus.NOT_FOUND, "Usuari no trobat amb id: " + id));
    }

    private Pista findPistaById(Integer id) {
        return pistaRepository.findById(id)
                .orElseThrow(() -> new BackendException(HttpStatus.NOT_FOUND, "Pista no trobada amb id: " + id));
    }
}
