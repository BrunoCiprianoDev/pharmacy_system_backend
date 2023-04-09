package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseService {

    void validatePurchase(Purchase purchase);

    Purchase savePurchase(Purchase purchase);

    Purchase updatePurchase(Purchase purchase);

    Page<Purchase> getPurchase(Pageable pageable);

    Purchase getPurchaseById(Long id);

    List<Purchase> getPurchaseBySupplierId(Long id);

    List<Purchase> findPurchaseByQuery(String query);

    void deletePurchase(Long id);

}
