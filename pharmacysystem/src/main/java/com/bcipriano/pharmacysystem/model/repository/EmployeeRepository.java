package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCpf(String cpf);

    Optional<Employee> findById(Long id);

    Optional<Employee> findByCpf(String cpf);

    boolean existsById(Long id);

    Page<Employee> findByPosition(Position position, Pageable pageable);

    @Query(value = "SELECT e FROM Employee e WHERE " +
            "e.email LIKE %:query% " +
            "OR (e.name LIKE %:query%) " +
            "OR (e.cpf LIKE %:query%) " +
            "OR (e.primaryPhone LIKE %:query%) " +
            "OR (e.secundaryPhone LIKE %:query%) " +
            "OR (e.email LIKE %:query%) ")
    Page<Employee> findEmployeesByQuery(@Param("query") String query, Pageable pageable);

}
