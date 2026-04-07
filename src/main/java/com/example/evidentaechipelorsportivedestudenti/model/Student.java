/**
 * Clasa entitate care defineste structura tabelului Studenti din baza de date
 * @author Cosmulete Ion-Cosmin
 * @version 07 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Studenti")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentID")
    private Integer studentID;

    @NotBlank(message = "Numele este obligatoriu!")
    @Size(min = 2, message = "Numele trebuie să aibă cel puțin 2 litere.")
    @Column(name = "Nume")
    private String nume;

    @NotBlank(message = "Prenumele este obligatoriu!")
    @Size(min = 2, message = "Prenumele trebuie să aibă cel puțin 2 litere.")
    @Column(name = "Prenume")
    private String prenume;

    @NotBlank(message = "Email-ul este obligatoriu!")
    @Email(message = "Formatul email-ului nu este valid (ex: nume@domeniu.com).")
    @Column(name = "Email")
    private String email;

    @NotBlank(message = "Numărul matricol este obligatoriu!")
    // ^ = început, \\d = cifră, {6} = exact 6 ori, $ = final
    @Pattern(regexp = "^\\d{6}$", message = "Numărul matricol trebuie să conțină exact 6 cifre!")
    @Column(name = "[Numar matricol]")
    private String numarMatricol;

    @NotNull(message = "Trebuie să selectezi o facultate!")
    @ManyToOne
    @JoinColumn(name = "FacultateID", nullable = false)
    private Facultate facultate;

    public Student() {}

    public Integer getStudentID() { return studentID; }
    public void setStudentID(Integer studentID) { this.studentID = studentID; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNumarMatricol() { return numarMatricol; }
    public void setNumarMatricol(String numarMatricol) { this.numarMatricol = numarMatricol; }

    public Facultate getFacultate() { return facultate; }
    public void setFacultate(Facultate facultate) { this.facultate = facultate; }
}