package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DiscountGroupRepository extends JpaRepository<DiscountGroup, Long> {

   @Query(value = "SELECT d FROM DiscountGroup d WHERE d.name LIKE %:query%")
    List<DiscountGroup> findDiscountGroupByQuery(@Param("query") String query);

    List<DiscountGroup> findByStartDate(LocalDate startDate);

    List <DiscountGroup> findByFinalDate(LocalDate finalDate);

    boolean existsByName(String name);

}
