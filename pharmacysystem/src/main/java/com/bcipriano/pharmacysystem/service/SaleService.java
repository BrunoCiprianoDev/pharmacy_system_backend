package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleService {

    void validateSale(Sale sale);

    void saveSale(Sale sale, List<SaleItem> saleItems);

    Sale updateSale(Sale sale);

    Page<Sale> getSale(Pageable pageable);

    Sale getSaleById(Long id);

    List<Sale> findSaleByQuery(String query);

    void deleteSale(Long id);

}
