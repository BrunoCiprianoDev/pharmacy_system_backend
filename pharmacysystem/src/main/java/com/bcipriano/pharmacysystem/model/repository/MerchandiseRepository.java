package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MerchandiseRepository extends JpaRepository<Merchandise, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Optional<Merchandise> findByCode(String code);

    List<Merchandise> findByDepartment(Department department);

    List<Merchandise> findByStripe(Stripe stripe);

    List<Merchandise> findByDiscountGroupId(@Param("discountGroupId") Long discountGroupId);

    @Query("SELECT m FROM Merchandise m JOIN m.discountGroup dg WHERE dg.name LIKE %:query%")
    List<Merchandise> findByDiscountGroupName(@Param("query") String query);

    @Query(value = "SELECT m FROM Merchandise m WHERE " +
            "m.name LIKE %:query% " +
            "OR m.code LIKE %:query% " +
            "OR m.classification LIKE %:query% " +
            "OR m.brand LIKE %:query% " +
            "OR m.formule LIKE %:query% " +
            "OR m.rms LIKE %:query% ")
    List<Merchandise> findMerchandiseByQuery(@Param("query") String query);

}
