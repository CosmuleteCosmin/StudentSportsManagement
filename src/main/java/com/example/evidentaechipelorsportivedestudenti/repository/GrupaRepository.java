/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.Grupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface GrupaRepository extends JpaRepository<Grupa, Integer> {

    @Query(value = "SELECT g.* FROM Grupe g " +
            "INNER JOIN Sporturi s ON g.SportID = s.SportID",
            nativeQuery = true)
    List<Grupa> gasesteToateGrupeleSQL();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Grupe (Nume, SportID) VALUES (:nume, :sportId)", nativeQuery = true)
    void insereazaGrupaSQL(@Param("nume") String nume, @Param("sportId") Integer sportId);

    @Query(value = "SELECT * FROM Grupe WHERE GrupaID = :id", nativeQuery = true)
    Grupa gasesteDupaIdSQL(@Param("id") Integer id);

    @Query(value = "SELECT g.* FROM Grupe g " +
            "LEFT JOIN Sporturi s ON g.SportID = s.SportID " +
            "WHERE g.Nume LIKE %:keyword% " +
            "OR s.Nume LIKE %:keyword%", nativeQuery = true)
    List<Grupa> cautaGrupeSQL(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Grupe SET Nume=:nume, SportID=:sid WHERE GrupaID=:id", nativeQuery = true)
    void actualizeazaGrupaSQL(@Param("id") Integer id, @Param("nume") String nume, @Param("sid") Integer sportId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Grupe WHERE GrupaID = :id", nativeQuery = true)
    void stergeGrupaSQL(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) FROM Grupe WHERE Nume = :nume", nativeQuery = true)
    int countByNumeSQL(@Param("nume") String nume);

    @Query(value = "SELECT COUNT(*) FROM Grupe WHERE Nume = :nume AND GrupaID != :id", nativeQuery = true)
    int countByNumeUpdateSQL(@Param("nume") String nume, @Param("id") Integer id);
}