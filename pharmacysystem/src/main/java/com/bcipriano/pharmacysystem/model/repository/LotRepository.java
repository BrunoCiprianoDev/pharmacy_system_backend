package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Long> {

    Optional<Lot> findLotById(Long id);

    Page<Lot> findLotsByPurchaseId(@Param("purchaseId") Long purchaseId, Pageable pageable);

    @Query("SELECT l FROM Lot l JOIN l.merchandise merchandise WHERE merchandise.id = :merchandiseId")
    Page<Lot> findLotsByMerchandiseId(@Param("merchandiseId") Long merchandiseId, Pageable pageable);

    @Query("SELECT l FROM Lot l JOIN l.purchase purchase WHERE purchase.noteNumber LIKE %:query%")
    Page<Lot> findLotsByPurchaseNoteNumber(@Param("query") String query, Pageable pageable);

    @Query("SELECT l FROM Lot l WHERE l.number LIKE %:query%")
    Page<Lot> findLotsByLotNumber(@Param("query") String query, Pageable pageable);

    @Query("SELECT l FROM Lot l WHERE l.merchandise.name LIKE %:query%")
    Page<Lot> findLotsByMerchandiseName(@Param("query") String query, Pageable pageable);

    Page<Lot>findByExpirationDate(LocalDate startDate, Pageable pageable);

    @Query("SELECT SUM(l.units) FROM Lot l WHERE l.merchandise.id = :merchandiseId")
    Integer sumUnitsByMerchandiseId(@Param("merchandiseId") Long merchandiseId);

    @Query("SELECT l FROM Lot l WHERE l.expirationDate BETWEEN :startDate AND :endDate")
    Page<Lot> findLotsExpiringWithinNDays(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    @Modifying
    @Query("update Lot l set l.units = l.units - :quantity where l.id = :lotId")
    void subtractUnitsFromLot(@Param("lotId") Long lotId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("update Lot l set l.units = l.units + :quantity where l.id = :lotId")
    void addUnitsFromLot(@Param("lotId") Long lotId, @Param("quantity") Integer quantity);

}