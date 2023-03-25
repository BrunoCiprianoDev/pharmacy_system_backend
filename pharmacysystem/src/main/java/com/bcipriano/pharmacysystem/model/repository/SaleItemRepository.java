package com.bcipriano.pharmacysystem.model.repository;

import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findSaleItemBySaleId(@Param("saleId") Long saleId);

    List<SaleItem> findSaleItemByLotId(@Param("lotId") Long lotId);

}
