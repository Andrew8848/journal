package com.ddmu.journal.repositories;

import com.ddmu.journal.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query(value = "SELECT * FROM action WHERE action.value = :value", nativeQuery = true)
    Action findByValue(@Param("value") String value);

}
