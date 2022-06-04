package com.ddmu.journal.repositories;


import com.ddmu.journal.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    @Query(value = "SELECT * FROM consultation WHERE consultation.value COLLATE utf8mb4_unicode_ci = :value", nativeQuery = true)
    Consultation findByValue(@Param("value") String value);

}
