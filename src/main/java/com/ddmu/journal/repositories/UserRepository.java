package com.ddmu.journal.repositories;

import com.ddmu.journal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value="SELECT * FROM user WHERE user.id COLLATE utf8mb4_unicode_ci =  :id", nativeQuery = true)
    public User getById(@Param("id") Long id);

    @Query(value = "SELECT user.* FROM user INNER JOIN doctor ON user.doctor_id =  doctor.id WHERE doctor.email COLLATE utf8mb4_unicode_ci =  :email", nativeQuery = true)
    public User findByEmail(@Param("email") String email);

    @Query(value = "SELECT EXISTS(SELECT * FROM doctor WHERE doctor.email COLLATE utf8mb4_unicode_ci = :email) AS Result", nativeQuery = true)
    public int emailIsExist(@Param("email") String email);

    @Query(value = "SELECT * FROM user INNER JOIN doctor ON user.doctor_id = doctor.id ORDER BY doctor.surname ASC, doctor.name ASC", nativeQuery = true)
    public Page<User> findAllUsers(Pageable pageable);

    @Query(value = "SELECT * FROM user INNER JOIN doctor ON user.doctor_id = doctor.id INNER JOIN user_roles ON user.id =  user_roles.user_id INNER JOIN role ON user_roles.role_id =  role.id WHERE role.value != \"USER\" ORDER BY doctor.surname ASC, doctor.name ASC", nativeQuery = true)
    public List<User> findAllElevatedUsers();

}
