/**
 * Clasa entitate care defineste structura tabelului Meciuri din baza de date
 * @author Cosmulete Ion-Cosmin
 * @version 08 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "Meciuri")
public class Meci {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MeciID")
    private Integer meciID;

    @NotNull(message = "Data este obligatorie!")
    @Column(name = "Data")
    private java.sql.Date data;

    @NotBlank(message = "Locația este obligatorie!")
    @Column(name = "Locatie")
    private String locatie;

    @NotNull(message = "Alege echipa gazdă!")
    @ManyToOne
    @JoinColumn(name = "Echipa1ID")
    private Echipa echipa1;

    @NotNull(message = "Alege echipa oaspete!")
    @ManyToOne
    @JoinColumn(name = "Echipa2ID")
    private Echipa echipa2;

    @NotNull(message = "Alege grupa!")
    @ManyToOne
    @JoinColumn(name = "GrupaID")
    private Grupa grupa;

    @Column(name = "[Scor echipa1]")
    @Min(value = 0, message = "Scorul nu poate fi negativ!")
    private Integer scorEchipa1;

    @Column(name = "[Scor echipa2]")
    @Min(value = 0, message = "Scorul nu poate fi negativ!")
    private Integer scorEchipa2;

    @ManyToOne
    @JoinColumn(name = "CastigatorID")
    private Echipa castigator;

    public Integer getMeciID() { return meciID; }
    public void setMeciID(Integer meciID) { this.meciID = meciID; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public String getLocatie() { return locatie; }
    public void setLocatie(String locatie) { this.locatie = locatie; }

    public Echipa getEchipa1() { return echipa1; }
    public void setEchipa1(Echipa echipa1) { this.echipa1 = echipa1; }

    public Echipa getEchipa2() { return echipa2; }
    public void setEchipa2(Echipa echipa2) { this.echipa2 = echipa2; }

    public Grupa getGrupa() { return grupa; }
    public void setGrupa(Grupa grupa) { this.grupa = grupa; }

    public Integer getScorEchipa1() { return scorEchipa1; }
    public void setScorEchipa1(Integer scorEchipa1) { this.scorEchipa1 = scorEchipa1; }

    public Integer getScorEchipa2() { return scorEchipa2; }
    public void setScorEchipa2(Integer scorEchipa2) { this.scorEchipa2 = scorEchipa2; }

    public Echipa getCastigator() { return castigator; }
    public void setCastigator(Echipa castigator) { this.castigator = castigator; }
}