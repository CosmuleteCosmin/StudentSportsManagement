/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.MembruEchipa;
import com.example.evidentaechipelorsportivedestudenti.model.MembruId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MembruEchipaRepository extends JpaRepository<MembruEchipa, MembruId> {

    @Query(value = "SELECT m.* FROM [Membrii Echipa] m " +
            "INNER JOIN Studenti s ON m.StudentID = s.StudentID " +
            "INNER JOIN Echipe e ON m.EchipaID = e.EchipaID",
            nativeQuery = true)
    List<MembruEchipa> gasesteTotiMembriiSQL();

    @Query(value = "SELECT * FROM [Membrii Echipa] WHERE StudentID = :sid AND EchipaID = :eid", nativeQuery = true)
    MembruEchipa gasesteDupaIdCompusSQL(@Param("sid") Integer studentId, @Param("eid") Integer echipaId);

    @Query(value = "SELECT me.* FROM [Membrii Echipa] me " +
            "LEFT JOIN Studenti st ON me.StudentID = st.StudentID " +
            "LEFT JOIN Echipe e ON me.EchipaID = e.EchipaID " +
            "LEFT JOIN Facultati f ON e.FacultateID = f.FacultateID " +
            "LEFT JOIN Sporturi sp ON e.SportID = sp.SportID " +
            "WHERE st.Nume LIKE %:keyword% " +
            "OR st.Prenume LIKE %:keyword% " +
            "OR me.Rol LIKE %:keyword% " +
            "OR f.Denumire LIKE %:keyword% " +
            "OR sp.Nume LIKE %:keyword%", nativeQuery = true)
    List<MembruEchipa> cautaMembriSQL(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE [Membrii Echipa] SET Rol = :rol WHERE StudentID = :sid AND EchipaID = :eid", nativeQuery = true)
    void actualizeazaRolMembruSQL(@Param("sid") Integer studentId, @Param("eid") Integer echipaId, @Param("rol") String rol);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM [Membrii Echipa] WHERE StudentID = :sid AND EchipaID = :eid", nativeQuery = true)
    void stergeMembruSQL(@Param("sid") Integer studentId, @Param("eid") Integer echipaId);

    @Query(value = "SELECT COUNT(*) FROM [Membrii Echipa] WHERE StudentID = :sid AND EchipaID = :eid", nativeQuery = true)
    int countDuplicateMemberSQL(@Param("sid") Integer sid, @Param("eid") Integer eid);

    @Query(value = "SELECT m.* FROM [Membrii Echipa] m " +
            "JOIN Studenti s ON m.StudentID = s.StudentID " +
            "JOIN Echipe e ON m.EchipaID = e.EchipaID " +
            "JOIN Facultati f ON e.FacultateID = f.FacultateID " +
            "WHERE (:facultateId IS NULL OR e.FacultateID = :facultateId) " +
            "AND (:sportId IS NULL OR e.SportID = :sportId) " +
            "AND (:rol IS NULL OR :rol = '' OR m.Rol = :rol) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(s.Nume) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.Prenume) LIKE LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<MembruEchipa> filtreazaSiCautaMembriSQL(@Param("facultateId") Integer facultateId,
                                                 @Param("sportId") Integer sportId,
                                                 @Param("rol") String rol,
                                                 @Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO [Membrii Echipa] (StudentID, EchipaID, Rol) VALUES (:sid, :eid, :rol)", nativeQuery = true)
    void insereazaMembruSQL(@Param("sid") Integer studentId, @Param("eid") Integer echipaId, @Param("rol") String rol);
}