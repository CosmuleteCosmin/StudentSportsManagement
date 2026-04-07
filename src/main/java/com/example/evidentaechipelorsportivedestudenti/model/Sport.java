/**
 * Clasa entitate care defineste structura tabelului Sporturi din baza de date
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Sporturi")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SportID")
    private Integer sportID;

    @NotBlank(message = "Numele sportului este obligatoriu!")
    @Column(name = "Nume")
    private String nume;

    @NotBlank(message = "Tipul sportului este obligatoriu (ex: Echipa/Individual)!")
    @Column(name = "Tip")
    private String tip;

    public Integer getSportID() { return sportID; }
    public void setSportID(Integer sportID) { this.sportID = sportID; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
}