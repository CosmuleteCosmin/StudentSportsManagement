/**
 * Interfata Repository care gestioneaza interogarile SQL Native pentru entitatea respectiva
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.repository;

import com.example.evidentaechipelorsportivedestudenti.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Interfață Repository pentru Student folosind SQL Nativ
 * @author Nume Student
 * @version 07 Ianuarie 2026
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query(value = "SELECT s.* FROM Studenti s " +
            "INNER JOIN Facultati f ON s.FacultateID = f.FacultateID",
            nativeQuery = true)
    List<Student> gasesteTotiStudentiiSQL();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Studenti (Nume, Prenume, Email, [Numar matricol], FacultateID) " +
            "VALUES (:nume, :prenume, :email, :nrMatricol, :facultateId)", nativeQuery = true)
    void insereazaStudentSQL(@Param("nume") String nume,
                             @Param("prenume") String prenume,
                             @Param("email") String email,
                             @Param("nrMatricol") String nrMatricol,
                             @Param("facultateId") Integer facultateId);

    @Query(value = "SELECT * FROM Studenti WHERE StudentID = :id", nativeQuery = true)
    Student gasesteDupaIdSQL(@Param("id") Integer id);

    @Query(value = "SELECT * FROM Studenti WHERE StudentID NOT IN (SELECT DISTINCT StudentID FROM [Membrii Echipa])", nativeQuery = true)
    List<Student> gasesteStudentiFaraEchipaSQL();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Studenti SET Nume=:n, Prenume=:p, Email=:e, [Numar matricol]=:nm, FacultateID=:fid WHERE StudentID=:id", nativeQuery = true)
    void actualizeazaStudentSQL(@Param("id") Integer id,
                                @Param("n") String nume, @Param("p") String prenume,
                                @Param("e") String email, @Param("nm") String nrMatricol,
                                @Param("fid") Integer facultateId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Studenti WHERE StudentID = :id", nativeQuery = true)
    void stergeStudentSQL(@Param("id") Integer id);

    // Verificari pentru duplicate

    @Query(value = "SELECT COUNT(*) FROM Studenti WHERE Email = :email", nativeQuery = true)
    int countByEmailSQL(@Param("email") String email);

    @Query(value = "SELECT COUNT(*) FROM Studenti WHERE [Numar matricol] = :matricol", nativeQuery = true)
    int countByMatricolSQL(@Param("matricol") String matricol);

    @Query(value = "SELECT COUNT(*) FROM Studenti WHERE Email = :email AND StudentID != :id", nativeQuery = true)
    int countByEmailUpdateSQL(@Param("email") String email, @Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) FROM Studenti WHERE [Numar matricol] = :matricol AND StudentID != :id", nativeQuery = true)
    int countByMatricolUpdateSQL(@Param("matricol") String matricol, @Param("id") Integer id);

    @Query(value = "SELECT main_s.* FROM Studenti main_s " +
            "WHERE main_s.StudentID IN (" +
            "SELECT sub_s.StudentID " +
            "FROM Studenti sub_s " +
            "LEFT JOIN Facultati f ON sub_s.FacultateID = f.FacultateID " +
            "WHERE (:fid IS NULL OR sub_s.FacultateID = :fid) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "sub_s.Nume LIKE CONCAT(:keyword, '%') OR " +
            "sub_s.Prenume LIKE CONCAT(:keyword, '%') OR " +
            "sub_s.Email LIKE CONCAT(:keyword, '%') OR " +
            "sub_s.[Numar matricol] LIKE CONCAT(:keyword, '%')) " +
            "GROUP BY sub_s.StudentID " +
            "HAVING COUNT(sub_s.StudentID) >= 1" + // AICI este funcția agregat
            ")",
            nativeQuery = true)
    List<Student> filtreazaSiCautaStudentiSQL(@Param("fid") Integer facultateId,
                                              @Param("keyword") String keyword);
}