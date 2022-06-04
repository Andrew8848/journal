package com.ddmu.journal.repositories;

import com.ddmu.journal.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM role WHERE role.value = :value", nativeQuery = true)
    Role findByValue(@Param("value") String value);

}
