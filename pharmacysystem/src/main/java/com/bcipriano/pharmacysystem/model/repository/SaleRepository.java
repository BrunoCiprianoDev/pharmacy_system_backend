package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByEmployeeId(@Param("employeeId") Long employeeId);

    List<Sale> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT s FROM Sale s " +
            "JOIN s.employee e " +
            "LEFT JOIN s.client c " +
            "WHERE e.name LIKE %:query% " +
            "OR c.name LIKE %:query% OR c IS NULL")
    List<Sale> findSaleByQuery(@Param("query") String query);

}