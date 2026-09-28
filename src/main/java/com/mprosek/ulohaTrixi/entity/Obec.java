package com.mprosek.ulohaTrixi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obec")
public class Obec {
    @Id
    private Integer kod;
    private String nazev;

    public Obec() {

    }

    public Obec(Integer kod, String nazev) {
        this.kod = kod;
        this.nazev = nazev;
    }

    // Gettery a Settery
    public Integer getKod() { return kod; }
    public void setKod(Integer kod) { this.kod = kod; }
    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
}
