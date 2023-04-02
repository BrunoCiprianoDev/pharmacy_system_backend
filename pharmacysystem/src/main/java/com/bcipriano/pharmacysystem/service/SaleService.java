package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Sale;

import java.util.List;

public interface SaleService {

    void validateSale(Sale sale);

    Sale saveSale(Sale sale);

    Sale updateSale(Sale sale);

    List<Sale> getSale();

    Sale getSaleById(Long id);

    List<Sale> findSaleByQuery(String query);

    void deleteSale(Long id);

}
