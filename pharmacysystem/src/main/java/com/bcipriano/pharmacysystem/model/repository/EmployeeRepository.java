package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findById(Long id);

    boolean existsById(Long id);

    List<Employee> findByProfile(Profile profile);

    @Query(value = "SELECT e FROM Employee e WHERE " +
            "e.email LIKE %:query% " +
            "OR (e.name LIKE %:query%) " +
            "OR (e.cpf LIKE %:query%) " +
            "OR (e.primaryPhone LIKE %:query%) " +
            "OR (e.secundaryPhone LIKE %:query%) ")
    List<Employee> findEmployeesByQuery(@Param("query") String query);

}

