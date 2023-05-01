package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.model.repository.PurchaseRepository;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseService {

    PurchaseRepository purchaseRepository;

    SupplierRepository supplierRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository){
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
    }

    public Page<Purchase> getPurchase(Pageable pageable) {
        return purchaseRepository.findAll(pageable);
    }

    public Page<Purchase> getPurchaseBySupplierId(Long id, Pageable pageable){
        return purchaseRepository.findPurchasesBySupplierId(id, pageable);
    }

    public Optional<Purchase> getPurchaseNoteNumber(String noteNumber) {
        if(!purchaseRepository.existsByNoteNumber(noteNumber)){
            throw new BusinessRuleException("Numero da nota fiscal inválido.");
        }
        Optional<Purchase> purchase = purchaseRepository.findByNoteNumber(noteNumber);
        return purchase;
    }

    public Optional<Purchase> getPurchaseById(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        if(purchase.isEmpty()){
            throw new NotFoundException("Compra com id inválido.");
        }
        return purchase;
    }

    public Page<Purchase> findPurchaseByQuery(String query, Pageable pageable) {
        return purchaseRepository.findPurchasesByNoteNumberByQuery(query, pageable);
    }

    public Purchase savePurchase(Purchase purchase) {
        if(purchaseRepository.existsByNoteNumber(purchase.getNoteNumber())){
            throw new BusinessRuleException("Ja existe uma compra cadastrada com esse número de nota.");
        }
        return purchaseRepository.save(purchase);
    }

    public Purchase updatePurchase(Purchase purchase) {
        if(!purchaseRepository.existsById(purchase.getId())){
            throw new NotFoundException("Compra com id inválido.");
        }

        Optional<Purchase> purchaseTest = purchaseRepository.findByNoteNumber(purchase.getNoteNumber());
        if(!purchaseTest.isEmpty() && purchaseTest.get().getId() != purchase.getId()){
            throw new BusinessRuleException("Erro ao atualizar o compra. Já existe outra compra com essa nota fiscal.");
        }

        return purchaseRepository.save(purchase);
    }

    public void deletePurchase(Long id) {
        if(!purchaseRepository.existsById(id)) {
            throw new NotFoundException("Cliente com id inválido.");
        }
        purchaseRepository.deleteById(id);
    }
}