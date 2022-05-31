package com.ddmu.journal.repositories;

import com.ddmu.journal.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    @Query(value = "SELECT * FROM log ORDER BY log.date DESC, log.time DESC", nativeQuery = true)
    Page<Log> getLogsByPage(Pageable pageable);

    @Query(value = "SELECT * FROM log INNER JOIN user ON log.user_id = user.id INNER JOIN doctor ON user.doctor_id = doctor.id WHERE doctor.email IN (:emails) ORDER BY log.date DESC, log.time DESC", nativeQuery = true)
    Page<Log> getLogsByUsersOnPage(Pageable pageable, @Param("emails") List<String> emails);
}
