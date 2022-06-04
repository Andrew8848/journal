package com.ddmu.journal.repositories;

import com.ddmu.journal.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "SELECT * FROM patient WHERE name COLLATE utf8mb4_unicode_ci =  :name AND surname COLLATE utf8mb4_unicode_ci =  :surname AND date_of_birth = :dateOfBirth", nativeQuery = true)
    public Patient findByNameAndSurname(@Param("name") String name, @Param("surname") String surname, @Param("dateOfBirth")Date dateOfBirth);

}
