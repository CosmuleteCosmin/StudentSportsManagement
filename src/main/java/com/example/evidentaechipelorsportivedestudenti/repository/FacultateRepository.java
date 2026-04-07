/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.Facultate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FacultateRepository extends JpaRepository<Facultate, Integer> {

    @Query(value = "SELECT * FROM Facultati", nativeQuery = true)
    List<Facultate> gasesteToateFacultatileSQL();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Facultati (denumire) VALUES (:denumire)", nativeQuery = true)
    void insereazaFacultateSQL(@Param("denumire") String denumire);

    @Query(value = "SELECT * FROM Facultati WHERE facultateID = :id", nativeQuery = true)
    Facultate gasesteDupaIdSQL(@Param("id") Integer id);

    @Query(value = "SELECT * FROM Facultati WHERE FacultateID NOT IN (SELECT DISTINCT FacultateID FROM Echipe)", nativeQuery = true)
    List<Facultate> gasesteFacultatiFaraEchipeSQL();

    @Query(value = "SELECT * FROM Facultati WHERE Denumire LIKE %:keyword%", nativeQuery = true)
    List<Facultate> cautaFacultatiSQL(@Param("keyword") String keyword);

    @Query(value = "SELECT COUNT(*) FROM Facultati WHERE Denumire = :denumire", nativeQuery = true)
    int countByDenumireSQL(@Param("denumire") String denumire);

    @Query(value = "SELECT COUNT(*) FROM Facultati WHERE Denumire = :denumire AND FacultateID != :id", nativeQuery = true)
    int countByDenumireUpdateSQL(@Param("denumire") String denumire, @Param("id") Integer id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Facultati SET denumire = :denumire WHERE facultateID = :id", nativeQuery = true)
    void actualizeazaFacultateSQL(@Param("id") Integer id, @Param("denumire") String denumire);

    @Query(value = "SELECT * FROM Facultati f " +
            "WHERE (SELECT COUNT(*) FROM Studenti s WHERE s.FacultateID = f.FacultateID) >= :minStudenti",
            nativeQuery = true)
    List<Facultate> filtreazaFacultatiDupaNrStudentiSQL(@Param("minStudenti") Integer minStudenti);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Facultati WHERE facultateID = :id", nativeQuery = true)
    void stergeFacultateSQL(@Param("id") Integer id);
}