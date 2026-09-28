package com.mprosek.ulohaTrixi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cast_obce")

public class CastObce {
    @Id
    private Integer kod;
    
    private String nazev;
    
    private Integer kodObce; // Odkaz na to, kam část patří

    public CastObce() {
    }

    public CastObce(Integer kod, String nazev, Integer kodObce) {
        this.kod = kod;
        this.nazev = nazev;
        this.kodObce = kodObce;
    }

    // Gettery a Settery
    public Integer getKod() { return kod; }
    public void setKod(Integer kod) { this.kod = kod; }
    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
    public Integer getKodObce() { return kodObce; }
    public void setKodObce(Integer kodObce) { this.kodObce = kodObce; }
    
}
