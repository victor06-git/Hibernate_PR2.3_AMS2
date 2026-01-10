package com.project.domain;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// TODO 1: @Entity
@Entity
public class Prestec implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO 2: @Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prestecId;

    // TODO 3: Relacions @ManyToOne (cap a Exemplar i Persona)
    @ManyToOne
    @JoinColumn(name = "exemplar_id")
    private Exemplar exemplar;
    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    private LocalDate dataPrestec;
    private LocalDate dataRetornPrevista;
    private LocalDate dataRetornReal; // Nullable

    private boolean actiu;

    public Prestec() {
    }

    public Prestec(Exemplar exemplar, Persona persona, LocalDate dataPrestec, LocalDate dataRetornPrevista) {
        this.exemplar = exemplar;
        this.persona = persona;
        this.dataPrestec = dataPrestec;
        this.dataRetornPrevista = dataRetornPrevista;
        this.actiu = true; // Per defecte, en crear-se està actiu
    }

    public Long getPrestecId() {
        return prestecId;
    }

    public void setPrestecId(Long prestecId) {
        this.prestecId = prestecId;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDate getDataPrestec() {
        return dataPrestec;
    }

    public void setDataPrestec(LocalDate dataPrestec) {
        this.dataPrestec = dataPrestec;
    }

    public LocalDate getDataRetornPrevista() {
        return dataRetornPrevista;
    }

    public void setDataRetornPrevista(LocalDate dataRetornPrevista) {
        this.dataRetornPrevista = dataRetornPrevista;
    }

    public LocalDate getDataRetornReal() {
        return dataRetornReal;
    }

    public void setDataRetornReal(LocalDate dataRetornReal) {
        this.dataRetornReal = dataRetornReal;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    @Override
    public String toString() {
        String nomPersona = (persona != null) ? persona.getNom() : "Desc.";
        String codiExemplar = (exemplar != null) ? exemplar.getCodiBarres() : "Desc.";
        return "Prestec{id=" + prestecId + ", exemplar=" + codiExemplar + ", persona=" + nomPersona + ", data="
                + dataPrestec + ", actiu=" + actiu + "}";
    }
}