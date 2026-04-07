/**
 * Clasa entitate care defineste tabela de legatura dintre studenti si echipe,
 * stocand rolul specific al fiecarui jucator (Capitan, Titular, Rezerva)
 * @author Cosmulete Ion-Cosmin
 * @version 08 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.model;

import java.io.Serializable;
import java.util.Objects;

public class MembruId implements Serializable {
    private Integer student;
    private Integer echipa;

    public MembruId() {}

    public MembruId(Integer student, Integer echipa) {
        this.student = student;
        this.echipa = echipa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembruId membruId = (MembruId) o;
        return Objects.equals(student, membruId.student) &&
                Objects.equals(echipa, membruId.echipa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, echipa);
    }
}