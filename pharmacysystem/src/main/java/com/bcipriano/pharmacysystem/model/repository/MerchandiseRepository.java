package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MerchandiseRepository extends JpaRepository<Merchandise, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Optional<Merchandise> findByCode(String code);

    Page<Merchandise> findByDepartment(Department department, Pageable pageable);

    Page<Merchandise> findByStripe(Stripe stripe, Pageable pageable);

    Page<Merchandise> findByDiscountGroupId(@Param("discountGroupId") Long discountGroupId, Pageable pageable);

    @Query("SELECT m FROM Merchandise m JOIN m.discountGroup dg WHERE dg.name LIKE %:query%")
    Page<Merchandise> findByDiscountGroupName(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT m FROM Merchandise m WHERE " +
            "m.name LIKE %:query% " +
            "OR m.code LIKE %:query% " +
            "OR m.classification LIKE %:query% " +
            "OR m.brand LIKE %:query% " +
            "OR m.formule LIKE %:query% " +
            "OR m.rms LIKE %:query% ")
    Page<Merchandise> findMerchandiseByQuery(@Param("query") String query, Pageable pagePage);

}