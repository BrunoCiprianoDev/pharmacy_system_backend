package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p WHERE p.noteNumber LIKE %:query%")
    List<Purchase> findPurchasesByNoteNumberByQuery(@Param("query") String query);

    List<Purchase> findPurchasesBySupplierId(@Param("supplierId") Long supplierId);

    boolean existsByNoteNumber(String noteNumber);

}
