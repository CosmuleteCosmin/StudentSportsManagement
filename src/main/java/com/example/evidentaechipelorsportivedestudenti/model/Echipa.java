/**
 * Clasa entitate care defineste structura tabelului Echipr
 * @author Cosmulete Ion-Cosmin
 * @version 07 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Echipe")
public class Echipa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EchipaID")
    private Integer echipaID;

    @NotNull(message = "Trebuie să alegi un sport!")
    @ManyToOne
    @JoinColumn(name = "SportID")
    private Sport sport;

    @NotNull(message = "Trebuie să alegi o facultate!")
    @ManyToOne
    @JoinColumn(name = "FacultateID")
    private Facultate facultate;

    public Integer getEchipaID() { return echipaID; }
    public void setEchipaID(Integer echipaID) { this.echipaID = echipaID; }

    public Sport getSport() { return sport; }
    public void setSport(Sport sport) { this.sport = sport; }

    public Facultate getFacultate() { return facultate; }
    public void setFacultate(Facultate facultate) { this.facultate = facultate; }
}