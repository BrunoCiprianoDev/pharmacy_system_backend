package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.model.repository.PurchaseRepository;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;
import com.bcipriano.pharmacysystem.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    PurchaseRepository purchaseRepository;

    SupplierRepository supplierRepository;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository){
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void validatePurchase(Purchase purchase) {

        if(purchase.getNoteNumber() == null || purchase.getNoteNumber().length() != 9){
            throw new BusinessRuleException("O número da nota fiscal não é valido.(Verifique os nove digitos)");
        }

        if(purchase.getTotal() < 0){
            throw new BusinessRuleException("Valor invalido para o total.");
        }

        if(!supplierRepository.existsById(purchase.getSupplier().getId())){
            throw new BusinessRuleException("Fornecedor inválido.");
        }

    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        if(purchaseRepository.existsByNoteNumber(purchase.getNoteNumber())){
            throw new BusinessRuleException("Ja existe uma compra cadastrada com esse número de nota.");
        }
        validatePurchase(purchase);
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase updatePurchase(Purchase purchase) {
        if(!purchaseRepository.existsById(purchase.getId())){
            throw new InvalidIdException();
        }
        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getPurchase() {
        return purchaseRepository.findAll();
    }

    @Override
    public Purchase getPurchaseById(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        if(purchase.isEmpty()){
            throw new InvalidIdException();
        }
        return purchase.get();
    }

    @Override
    public List<Purchase> findPurchaseByQuery(String query) {
        return purchaseRepository.findPurchasesByNoteNumberByQuery(query);
    }

    @Override
    public void deletePurchase(Long id) {
        if(!purchaseRepository.existsById(id)) {
            throw new InvalidIdException();
        }
        purchaseRepository.deleteById(id);
    }
}
