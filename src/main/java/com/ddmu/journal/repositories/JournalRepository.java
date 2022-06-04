package com.ddmu.journal.repositories;

import com.ddmu.journal.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

    @Query(value="SELECT * FROM journal WHERE journal.id COLLATE utf8mb4_unicode_ci =  :id", nativeQuery = true)
    Journal getById(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM journal INNER JOIN journal_status ON journal.journal_status_id = journal_status.id WHERE journal_status.value COLLATE utf8mb4_unicode_ci = :journal_status", nativeQuery = true)
    long countJournalsByStatus(@Param("journal_status") String journalStatus);

    @Query(value = "SELECT * FROM journal INNER JOIN journal_status ON journal.journal_status_id =  journal_status.id WHERE journal_status.value IN (:filter) ORDER BY journal.date_publication DESC, journal.time_publication DESC", nativeQuery = true)
    Page<Journal> findNewestJournalsByDateTimeOnPage(Pageable pageable, @Param("filter") List<String> filter);

    @Query(value = "SELECT * FROM journal INNER JOIN doctor ON journal.doctor_id = doctor.id INNER JOIN journal_status ON journal.journal_status_id = journal_status.id WHERE doctor.email IN (:emails) AND journal_status.value IN (:filter) ORDER BY journal.date_publication DESC, journal.time_publication DESC", nativeQuery = true)
    Page<Journal> findNewestJournalsByEmailsAndDateTimeOnPage(Pageable pageable, @Param("emails") List<String> emails, @Param("filter") List<String> filter);


    @Query(value = "SELECT * FROM journal ORDER BY journal.date_publication ASC, journal.time_publication ASC", nativeQuery = true)
    Page<Journal> findOldestJournalsByDateTimeOnPage(Pageable pageable);

}
