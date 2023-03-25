package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Loss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LossRepository extends JpaRepository<Loss, Long> {

    @Query("SELECT l FROM Loss l JOIN l.employee e WHERE e.name LIKE %:query%")
    List<Loss> findByEmployeeName(@Param("query") String query);

    List<Loss> findLossByLotId(@Param("lotId") Long lotId);

    @Query("SELECT l FROM Loss l JOIN l.lot lot WHERE lot.number LIKE %:query%")
    List<Loss> findLossByLotNumber(@Param("query") String query);

}
