package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Purchase;

import java.util.List;

public interface PurchaseService {

    void validatePurchase(Purchase purchase);

    Purchase savePurchase(Purchase purchase);

    Purchase updatePurchase(Purchase purchase);

    List<Purchase> getPurchase();

    Purchase getPurchaseById(Long id);

    List<Purchase> findPurchaseByQuery(String query);

    void deletePurchase(Long id);

}
