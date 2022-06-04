package com.ddmu.journal.repositories;

import com.ddmu.journal.model.JournalStatus;
import com.ddmu.journal.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    @Query(value = "SELECT * FROM user_status WHERE user_status.value COLLATE utf8mb4_unicode_ci = :value", nativeQuery = true)
    UserStatus findByValue(@Param("value") String value);
}
