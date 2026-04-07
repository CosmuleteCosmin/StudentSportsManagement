/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.Echipa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface EchipaRepository extends JpaRepository<Echipa, Integer> {

    @Query(value = "SELECT e.* FROM Echipe e " +
            "INNER JOIN Facultati f ON e.FacultateID = f.FacultateID " +
            "INNER JOIN Sporturi s ON e.SportID = s.SportID",
            nativeQuery = true)
    List<Echipa> gasesteToateEchipeleSQL();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Echipe (SportID, FacultateID) VALUES (:sportId, :facultateId)", nativeQuery = true)
    void insereazaEchipaSQL(@Param("sportId") Integer sportId,
                            @Param("facultateId") Integer facultateId);

    @Query(value = "SELECT e.* FROM Echipe e " +
            "INNER JOIN Sporturi s ON e.SportID = s.SportID " +
            "WHERE e.EchipaID = :id",
            nativeQuery = true)
    List<Echipa> gasesteEchipeDupaFacultate (@Param("facultateId") Integer facultateId);

    @Query(value = "SELECT * FROM Echipe WHERE EchipaID = :id", nativeQuery = true)
    Echipa gasesteDupaIdSQL(@Param("id") Integer id);

    @Query(value = "SELECT * FROM Echipe WHERE EchipaID IN (SELECT Echipa1ID FROM Meciuri) OR EchipaID IN (SELECT Echipa2ID FROM Meciuri)", nativeQuery = true)
    List<Echipa> gasesteEchipeCuMeciuriSQL();

    @Query(value = "SELECT e.* FROM Echipe e " +
            "LEFT JOIN Facultati f ON e.FacultateID = f.FacultateID " +
            "LEFT JOIN Sporturi s ON e.SportID = s.SportID " +
            "WHERE f.Denumire LIKE %:keyword% " +
            "OR s.Nume LIKE %:keyword%", nativeQuery = true)
    List<Echipa> cautaEchipeSQL(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Echipe SET SportID = :sid, FacultateID = :fid WHERE EchipaID = :id", nativeQuery = true)
    void actualizeazaEchipaSQL(@Param("id") Integer id, @Param("sid") Integer sportId, @Param("fid") Integer facultateId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Echipe WHERE EchipaID = :id", nativeQuery = true)
    void stergeEchipaSQL(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) FROM Echipe WHERE FacultateID = :fid AND SportID = :sid", nativeQuery = true)
    int countByFacultateAndSportSQL(@Param("fid") Integer fid, @Param("sid") Integer sid);

    @Query(value = "SELECT e.* FROM Echipe e " +
            "JOIN Sporturi s ON e.SportID = s.SportID " +
            "JOIN Facultati f ON e.FacultateID = f.FacultateID " +
            "WHERE (:sportId IS NULL OR e.SportID = :sportId) " +
            "AND (:facultateId IS NULL OR e.FacultateID = :facultateId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(s.Nume) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.Denumire) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Echipa> filtreazaSiCautaEchipeSQL(@Param("sportId") Integer sportId,
                                           @Param("facultateId") Integer facultateId,
                                           @Param("keyword") String keyword);

    @Query(value = "SELECT COUNT(*) FROM Echipe WHERE FacultateID = :fid AND SportID = :sid AND EchipaID != :id", nativeQuery = true)
    int countByFacultateAndSportUpdateSQL(@Param("fid") Integer fid, @Param("sid") Integer sid, @Param("id") Integer id);
}