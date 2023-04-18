package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Loss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LossRepository extends JpaRepository<Loss, Long> {

    /*@Query("SELECT l FROM Loss l JOIN l.employee e WHERE e.name LIKE %:query%")
    Page<Loss> findByEmployeeName(@Param("query") String query, Pageable pageable);*/

    Page<Loss> findLossByLotId(@Param("lotId") Long lotId, Pageable pageable);

    @Query("SELECT l FROM Loss l JOIN l.lot lot WHERE lot.number LIKE %:query%")
    Page<Loss> findLossByLotNumber(@Param("query") String query, Pageable pageable);

    Optional<Loss> findLossById(@Param("id") Long id);

}