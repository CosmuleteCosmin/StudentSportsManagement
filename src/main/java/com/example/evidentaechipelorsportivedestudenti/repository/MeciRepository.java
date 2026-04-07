/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.Meci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface MeciRepository extends JpaRepository<Meci, Integer> {

    @Query(value = "SELECT m.* FROM Meciuri m " +
            "INNER JOIN Echipe e1 ON m.Echipa1ID = e1.EchipaID " +
            "INNER JOIN Echipe e2 ON m.Echipa2ID = e2.EchipaID " +
            "INNER JOIN Grupe g ON m.GrupaID = g.GrupaID",
            nativeQuery = true)
    List<Meci> gasesteToateMeciurileSQL();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Meciuri (Echipa1ID, Echipa2ID, GrupaID, Data, Locatie) " +
            "VALUES (:e1, :e2, :gId, :data, :loc)", nativeQuery = true)
    void insereazaMeciSQL(@Param("e1") Integer echipa1Id,
                          @Param("e2") Integer echipa2Id,
                          @Param("gId") Integer grupaId,
                          @Param("data") Date data,
                          @Param("loc") String locatie);

    @Query(value = "SELECT * FROM Meciuri WHERE MeciID = :id", nativeQuery = true)
    Meci gasesteDupaIdSQL(@Param("id") Integer id);

    @Query(value = "SELECT m.* FROM Meciuri m " +
            "LEFT JOIN Echipe e1 ON m.Echipa1ID = e1.EchipaID " +
            "LEFT JOIN Facultati f1 ON e1.FacultateID = f1.FacultateID " +
            "LEFT JOIN Echipe e2 ON m.Echipa2ID = e2.EchipaID " +
            "LEFT JOIN Facultati f2 ON e2.FacultateID = f2.FacultateID " +
            "LEFT JOIN Grupe g ON m.GrupaID = g.GrupaID " +
            "WHERE m.Locatie LIKE %:keyword% " +
            "OR CAST(m.Data AS VARCHAR) LIKE %:keyword% " +
            "OR f1.Denumire LIKE %:keyword% " +
            "OR f2.Denumire LIKE %:keyword% " +
            "OR g.Nume LIKE %:keyword%", nativeQuery = true)
    List<Meci> cautaMeciuriSQL(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Meciuri SET Echipa1ID=:e1, Echipa2ID=:e2, GrupaID=:gid, Data=:d, Locatie=:loc WHERE MeciID=:id", nativeQuery = true)
    void actualizeazaMeciSQL(@Param("id") Integer id, @Param("e1") Integer e1, @Param("e2") Integer e2,
                             @Param("gid") Integer gid, @Param("d") Date data, @Param("loc") String loc);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Meciuri WHERE MeciID = :id", nativeQuery = true)
    void stergeMeciSQL(@Param("id") Integer id);

    @Query(value = "SELECT * FROM Meciuri WHERE Data >= CAST(GETDATE() AS DATE) ORDER BY Data ASC", nativeQuery = true)
    List<Meci> gasesteMeciuriViitoareSQL();

    @Query(value = "SELECT f.Denumire, COUNT(m.MeciID) " +
            "FROM Facultati f " +
            "LEFT JOIN Echipe e ON f.FacultateID = e.FacultateID " +
            "LEFT JOIN Meciuri m ON e.EchipaID = m.CastigatorID " +
            "GROUP BY f.FacultateID, f.Denumire " +
            "ORDER BY COUNT(m.MeciID) DESC", nativeQuery = true)
    List<Object[]> getClasamentGeneral();

    @Query(value = "SELECT main_m.* FROM Meciuri main_m " +
            "WHERE main_m.MeciID IN (" +
            "SELECT sub_m.MeciID " +
            "FROM Meciuri sub_m " +
            "INNER JOIN Echipe e1 ON sub_m.Echipa1ID = e1.EchipaID " +
            "INNER JOIN Echipe e2 ON sub_m.Echipa2ID = e2.EchipaID " +
            "INNER JOIN Facultati f1 ON e1.FacultateID = f1.FacultateID " +
            "INNER JOIN Facultati f2 ON e2.FacultateID = f2.FacultateID " +
            "WHERE (:echipa1Id IS NULL OR sub_m.Echipa1ID = :echipa1Id) " +
            "AND (:echipa2Id IS NULL OR sub_m.Echipa2ID = :echipa2Id) " +
            "AND (:data IS NULL OR sub_m.Data = :data) " +
            "AND (:locatie IS NULL OR :locatie = '' OR LOWER(sub_m.Locatie) LIKE LOWER(CONCAT('%', :locatie, '%'))) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(f1.Denumire) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f2.Denumire) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sub_m.Locatie) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "GROUP BY sub_m.MeciID " +
            "HAVING COUNT(sub_m.MeciID) >= 1" + // AICI este functia agregat
            ")",
            nativeQuery = true)
    List<Meci> filtreazaSiCautaMeciuriSQL(@Param("echipa1Id") Integer echipa1Id,
                                          @Param("echipa2Id") Integer echipa2Id,
                                          @Param("locatie") String locatie,
                                          @Param("data") java.sql.Date data,
                                          @Param("keyword") String keyword);

    @Query(value = "SELECT COUNT(*) FROM Meciuri WHERE Echipa1ID = :e1 AND Echipa2ID = :e2 AND Data = :data", nativeQuery = true)
    int countDuplicateMatchSQL(@Param("e1") Integer e1, @Param("e2") Integer e2, @Param("data") Date data);

    @Query(value = "SELECT COUNT(*) FROM Meciuri WHERE Echipa1ID = :e1 AND Echipa2ID = :e2 AND Data = :data AND MeciID != :id", nativeQuery = true)
    int countDuplicateMatchUpdateSQL(@Param("e1") Integer e1, @Param("e2") Integer e2, @Param("data") Date data, @Param("id") Integer id);
}