package com.easypadel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "reserves")
public class Reserva {

    public static final int TORN_MIN = 1;
    public static final int TORN_MAX = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuari_id")
    private Usuari usuari;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pista_id")
    private Pista pista;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private byte torn;

    public Integer getId() {
        return id;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public Pista getPista() {
        return pista;
    }

    public void setPista(Pista pista) {
        this.pista = pista;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getTorn() {
        return torn;
    }

    public void setTorn(int torn) {
        this.torn = (byte) torn;
    }
}
