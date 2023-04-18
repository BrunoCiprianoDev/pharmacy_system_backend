package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p WHERE p.noteNumber LIKE %:query%")
    Page<Purchase> findPurchasesByNoteNumberByQuery(@Param("query") String query, Pageable pageable);

    Page<Purchase> findPurchasesBySupplierId(@Param("supplierId") Long supplierId, Pageable pageable);

    boolean existsByNoteNumber(String noteNumber);

    Optional<Purchase> findByNoteNumber(String noteNumber);

}