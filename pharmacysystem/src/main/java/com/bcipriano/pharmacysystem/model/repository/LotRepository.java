package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {

    List<Lot> findLotsByPurchaseId(@Param("purchaseId") Long purchaseId);

    @Query("SELECT l FROM Lot l JOIN l.merchandise merchandise WHERE merchandise.id = :merchandiseId")
    List<Lot> findLotsByMerchandiseId(@Param("merchandiseId") Long merchandiseId);

    @Query("SELECT l FROM Lot l JOIN l.purchase purchase WHERE purchase.noteNumber LIKE %:query%")
    List<Lot> findLotsByPurchaseNoteNumber(@Param("query") String query);

    @Query("SELECT l FROM Lot l WHERE l.number LIKE %:query%")
    List<Lot> findLotsByLotNumber(@Param("query") String query);

    List<Lot>findByExpirationDate(LocalDate startDate);

}
