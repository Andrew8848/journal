package com.ddmu.journal.repositories;

import com.ddmu.journal.model.JournalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalStatusRepository extends JpaRepository<JournalStatus, Long> {
    @Query(value = "SELECT * FROM journal_status WHERE journal_status.value COLLATE utf8mb4_unicode_ci = :value", nativeQuery = true)
    JournalStatus findByValue(@Param("value") String value);
}
