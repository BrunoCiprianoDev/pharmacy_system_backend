package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleService {

    void validateSale(Sale sale);

    Sale saveSale(Sale sale);

    Sale updateSale(Sale sale);

    Page<Sale> getSale(Pageable pageable);

    Sale getSaleById(Long id);

    List<Sale> findSaleByQuery(String query);

    void deleteSale(Long id);

}
