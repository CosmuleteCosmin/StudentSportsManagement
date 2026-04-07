/**
 * Clasa entitate care defineste structura tabelului [Membrii Echipa] din baza de date
 * @author Cosmulete Ion-Cosmin
 * @version 08 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Membrii Echipa")
@IdClass(MembruId.class)
public class MembruEchipa {

    @Id
    @ManyToOne
    @JoinColumn(name = "StudentID")
    private Student student;

    @Id
    @ManyToOne
    @JoinColumn(name = "EchipaID")
    private Echipa echipa;

    @Column(name = "Rol")
    private String rol;

    public MembruEchipa() {}

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Echipa getEchipa() { return echipa; }
    public void setEchipa(Echipa echipa) { this.echipa = echipa; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}