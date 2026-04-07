/**
 * Clasa entitate care defineste structura tabelului Facultatt
 * @author Cosmulete Ion-Cosmin
 * @version 08 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Facultati")
public class Facultate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facultateID;

    @NotBlank(message = "Numele facultății este obligatoriu!")
    @Size(min = 3, message = "Denumirea trebuie să aibă minim 3 caractere.")
    @Column(name = "Denumire")
    private String denumire;

    public Integer getFacultateID() {
        return facultateID;
    }

    public void setFacultateID(Integer facultateID) {
        this.facultateID = facultateID;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}