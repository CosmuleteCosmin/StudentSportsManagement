/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {

    @Query(value = "SELECT * FROM Sporturi", nativeQuery = true)
    List<Sport> gasesteToateSporturileSQL();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Sporturi (Nume, Tip) VALUES (:nume, :tip)", nativeQuery = true)
    void insereazaSportSQL(@Param("nume") String nume, @Param("tip") String tip);

    @Query(value = "SELECT * FROM Sporturi WHERE SportID = :id", nativeQuery = true)
    Sport gasesteDupaIdSQL(@Param("id") Integer id);

    @Query(value = "SELECT * FROM Sporturi WHERE SportID NOT IN (SELECT DISTINCT SportID FROM Echipe)", nativeQuery = true)
    List<Sport> gasesteSporturiFaraEchipeSQL();

    @Query(value = "SELECT * FROM Sporturi WHERE Nume LIKE %:keyword% OR Tip LIKE %:keyword%", nativeQuery = true)
    List<Sport> cautaSporturiSQL(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Sporturi SET Nume = :nume, Tip = :tip WHERE SportID = :id", nativeQuery = true)
    void actualizeazaSportSQL(@Param("id") Integer id, @Param("nume") String nume, @Param("tip") String tip);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Sporturi WHERE SportID = :id", nativeQuery = true)
    void stergeSportSQL(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) FROM Sporturi WHERE Nume = :nume", nativeQuery = true)
    int countByNumeSQL(@Param("nume") String nume);

    @Query(value = "SELECT s.Nume, " +
            "(SELECT COUNT(me.StudentID) " +
            " FROM [Membrii Echipa] me " +
            " INNER JOIN Echipe e ON me.EchipaID = e.EchipaID " +
            " WHERE e.SportID = s.SportID) AS NumarJucatori " +
            "FROM Sporturi s " +
            "ORDER BY NumarJucatori DESC",
            nativeQuery = true)
    List<Object[]> getJucatoriPerSport();

    @Query(value = "SELECT * FROM Sporturi " +
            "WHERE (:tip IS NULL OR :tip = '' OR Tip = :tip) " +
            "AND (:keyword IS NULL OR :keyword = '' OR LOWER(Nume) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Sport> filtreazaSiCautaSporturiSQL(@Param("tip") String tip, @Param("keyword") String keyword);

    @Query(value = "SELECT COUNT(*) FROM Sporturi WHERE Nume = :nume AND SportID != :id", nativeQuery = true)
    int countByNumeUpdateSQL(@Param("nume") String nume, @Param("id") Integer id);
}