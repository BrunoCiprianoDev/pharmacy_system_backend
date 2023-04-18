package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsById(Long id);

    boolean existsByCnpj(String cnpj);

    Optional<Supplier> findByCnpj(String cnpj);

    @Query(value = "SELECT s FROM Supplier s WHERE " +
            "s.email LIKE %:query% " +
            "OR (s.name LIKE %:query%) " +
            "OR (s.cnpj LIKE %:query%) " +
            "OR (s.primaryPhone LIKE %:query%) " +
            "OR (s.secundaryPhone LIKE %:query%) " +
            "OR (s.email LIKE %:query%) ")
    Page<Supplier> findSupplierByQuery(@Param("query") String query, Pageable pageable);

}
