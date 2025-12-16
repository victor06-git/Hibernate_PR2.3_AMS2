package com.project.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

// TODO 1: @Entity
@Entity
public class Biblioteca implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO 2: @Id
    @Id
    private Long bibliotecaId;

    private String nom;
    private String ciutat;
    private String adreca;
    private String telefon;
    private String email;

    // TODO 3: @OneToMany cap a Exemplar
    @OneToMany(mappedBy = "biblioteca")
    private Set<Exemplar> exemplars = new HashSet<>();

    public Biblioteca() {
    }

    public Biblioteca(String nom, String ciutat, String adreca, String telefon, String email) {
        this.nom = nom;
        this.ciutat = ciutat;
        this.adreca = adreca;
        this.telefon = telefon;
        this.email = email;
    }

    public Long getBibliotecaId() {
        return bibliotecaId;
    }

    public void setBibliotecaId(Long bibliotecaId) {
        this.bibliotecaId = bibliotecaId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCiutat() {
        return ciutat;
    }

    public void setCiutat(String ciutat) {
        this.ciutat = ciutat;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Exemplar> getExemplars() {
        return exemplars;
    }

    public void setExemplars(Set<Exemplar> exemplars) {
        this.exemplars = exemplars;
    }

    @Override
    public String toString() {
        return "Biblioteca{id=" + bibliotecaId + ", nom='" + nom + "', ciutat='" + ciutat + "'}";
    }
}