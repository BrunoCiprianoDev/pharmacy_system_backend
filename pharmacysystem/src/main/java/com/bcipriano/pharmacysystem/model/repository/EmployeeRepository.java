package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

  /*  @Query(value = "SELECT employee FROM  management.employee e " +
            "WHERE (e.email LIKE %:query%) " +
            "OR (e.name LIKE %:query%) " +
            "OR (e.profile LIKE %:query%)")
    List<Employee> findEmployeesByQuery(@Param("query") String query);*/

}

/*
* @Query("SELECT employee FROM Employee employee " +
            "WHERE (:email is null or employee.email LIKE %:query%) " +
            "OR (:name is null or employee.name LIKE %:query%) " +
            "OR (:profile is null or employee.profile LIKE %:query%)")
    List<Employee> findEmployeesByQuery(@Param("query") String query);
*
* */
