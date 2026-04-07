/**
 * Clasa entitate care defineste structura tabelului Grupe din baza de date
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Grupe")
public class Grupa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GrupaID")
    private Integer grupaID;

    @NotBlank(message = "Numele grupei este obligatoriu!")
    @Column(name = "Nume")
    private String nume;

    @NotNull(message = "Trebuie să alegi un sport!")
    @ManyToOne
    @JoinColumn(name = "SportID")
    private Sport sport;

    public Integer getGrupaID() { return grupaID; }
    public void setGrupaID(Integer grupaID) { this.grupaID = grupaID; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public Sport getSport() { return sport; }
    public void setSport(Sport sport) { this.sport = sport; }
}