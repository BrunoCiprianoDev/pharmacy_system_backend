package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface DiscountGroupRepository extends JpaRepository<DiscountGroup, Long> {

    @Query(value = "SELECT d FROM DiscountGroup d WHERE d.name LIKE %:query%")
    Page<DiscountGroup> findDiscountGroupByQuery(@Param("query") String query, Pageable pageable);

    Page<DiscountGroup> findByStartDate(LocalDate startDate, Pageable pageable);

    Page<DiscountGroup> findByFinalDate(LocalDate finalDate, Pageable pageable);

    boolean existsByName(String name);

}