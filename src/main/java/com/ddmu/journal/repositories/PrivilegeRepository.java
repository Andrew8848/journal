package com.ddmu.journal.repositories;

import com.ddmu.journal.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Query(value = "SELECT * FROM privilege WHERE privilege.value = :value", nativeQuery = true)
    Privilege findByValue(@Param("value") String value);

}
